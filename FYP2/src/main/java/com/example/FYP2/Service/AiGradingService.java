package com.example.FYP2.Service;


import com.example.FYP2.DTO.GamePlayerDTO;
import com.example.FYP2.DTO.QuestionDTO;
import com.example.FYP2.DTO.ReqRes;
import com.example.FYP2.Entity.GamePlayer;
import com.example.FYP2.Repository.GamePlayerRepo;
import com.example.FYP2.Repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class AiGradingService {

    @Value("${GeminiAPIKey}")
    private String apiKey;

    @Autowired
    private LocalEmbeddingService localEmbeddingService;

    private final RestTemplate restTemplate = new RestTemplate();

    // ─────────────────────────────────────────
    // MAIN METHOD
    // ─────────────────────────────────────────
    public double gradeAnswer(QuestionDTO question, String studentAnswer) throws Exception {

        double keywordScore  = gradeByKeyword(question.getKeyword(), studentAnswer);
        double semanticScore = gradeBySemanticText(question, studentAnswer);


        System.out.println("Keyword Score: "  + keywordScore);
        System.out.println("Semantic Score: " + semanticScore);

        double finalScore = 0;

        if(semanticScore == 0.0)
        {
            finalScore = keywordScore;
        }
        else {

            finalScore = (keywordScore * 0.5) + (semanticScore * 0.5);
        }

        return finalScore * question.getMaxMark();
    }

    // ─────────────────────────────────────────
    // KEYWORD GRADING — Local Embedding
    // each keyword vs whole student answer
    // ─────────────────────────────────────────
    private double gradeByKeyword(String modelKeywords, String studentAnswer) throws Exception {

        String[] modelKeywordList = modelKeywords.split(",");

        int keywordMarks = 1;

        double final_totalScore =0;

        // embed student answer once only
        double[] studentVector = null;
        String studentAnswerLower = studentAnswer.toLowerCase();


        double totalScore = 0.0;

        for (String modelKeyword : modelKeywordList) {

            String kw = modelKeyword.trim();
            String kwLower = kw.toLowerCase();
            double similarity;

            // ── Step 1: direct substring check (cheap, no AI needed) ──
            if (studentAnswerLower.contains(kwLower)) {
                similarity = 1.0;
                System.out.println("'" + kw + "' → exact match: 1.0 ✓");
            }
            else {
                // lazy-load student vector only when needed
                if (studentVector == null) {
                    studentVector = localEmbeddingService.getEmbedding(studentAnswer);
                }

                double[] keywordVector = localEmbeddingService.getEmbedding(kw);
                similarity = localEmbeddingService.cosineSimilarity(keywordVector, studentVector);
                System.out.println("'" + kw + "' → embedding similarity: " + similarity);
            }

            if(similarity > 0.35)
            {

                totalScore += keywordMarks;
            }
        }

        return totalScore / modelKeywordList.length;
    }


    // ─────────────────────────────────────────
    // SEMANTIC GRADING — Gemini Text
    // judge overall meaning
    // ─────────────────────────────────────────
    private double gradeBySemanticText(QuestionDTO question, String studentAnswer) {

        try{
            Thread.sleep(2000);// 1 second gap between calls
        }catch (Exception err)
        {
            Thread.currentThread().interrupt();
        }

        System.out.println("THis is gradeBySemanticText Function");

        String prompt = """
                You are a strict and accurate biology answer grader do not leniency to the student.
                Model Answer: %s 
                Student Answer: %s 
                
                Grade how semantically and scientifically similar the student answer
                is compared to the model answer.
                
                Evaluation Rules:
                - Consider biological correctness above all else.
                - Accept paraphrased answers if the core biological concept is scientifically correct.
                - Ignore grammar and spelling mistakes.
                - Penalize incorrect biological facts.
                - Penalize missing important biological concepts.
                - Do not reward vague, incomplete, or irrelevant information.
                - Be strict. Do not be generous with scores.
                
                Scoring Guide:
                0.95 - 1.00 = Almost identical
                0.85 - 0.94 = Very similar
                0.70 - 0.84 = Related / acceptable
                0.50 - 0.69 = Loosely related
                0.30 - 0.49 = Weak understanding
                0.10 - 0.29 = Mostly incorrect
                0.00 - 0.09 = Completely wrong 
                
                IMPORTANT:
                - Reply with ONLY one number between 0.00 and 1.00.
                - Use exactly TWO decimal places.
                - Do not explain your answer.
                - Do not output extra text. 
                
                """.formatted(
//                question.getQuestionText(),
                question.getAnswerText(),
                studentAnswer
        );

        return callGeminiScore(prompt);
    }


    // ─────────────────────────────────────────
    // CALL GEMINI — returns score (0.0 - 1.0)
    // ─────────────────────────────────────────
    private double callGeminiScore(String prompt) {
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            String result = "";
            try {
                System.out.println("THis is callGeminiScore Function (attempt " + attempt + ")");
                result = callGeminiText(prompt);

                if (result == null || result.isBlank()) {
                    System.out.println("Gemini returned empty, retrying in " + (attempt * 1000) + "ms...");
                    Thread.sleep(attempt * 1000L);
                    continue;
                }

                return Double.parseDouble(result.trim());

            } catch (NumberFormatException e) {
                System.out.println("Gemini parse error, raw: '" + result + "'");
                return 0.0;
            } catch (Exception e) {
                System.out.println("Gemini error attempt " + attempt + ": " + e.getMessage());
                try { Thread.sleep(attempt * 1000L); } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return 0.0;
    }


    // ─────────────────────────────────────────
    // CALL GEMINI — returns text
    // ─────────────────────────────────────────
    private String callGeminiText(String prompt) {

        System.out.println("THis is callGeminiText Function");


        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + "gemini-2.5-flash-lite:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);

            // ← add this to see the full response when something goes wrong
            if (response.getBody() == null) {
                System.out.println("Gemini returned null body");
                return "";
            }

            String extracted = extractTextFromResponse(response.getBody());
            System.out.println("Gemini raw response: '" + extracted + "'");  // ← add this
            return extracted;
        } catch (Exception e) {
            System.out.println("Gemini error: " + e.getMessage());
            return "";
        }
    }


    // ─────────────────────────────────────────
    // HELPER — extract text from Gemini response
    // ─────────────────────────────────────────
    private String extractTextFromResponse(Map responseBody) {
        List<Map> candidates = (List<Map>) responseBody.get("candidates");
        Map content          = (Map) candidates.get(0).get("content");
        List<Map> parts      = (List<Map>) content.get("parts");
        return (String) parts.get(0).get("text");
    }

}
