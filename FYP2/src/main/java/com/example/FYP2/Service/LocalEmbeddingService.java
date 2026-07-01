package com.example.FYP2.Service;


import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.LongBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocalEmbeddingService {


    private HuggingFaceTokenizer tokenizer;
    private OrtEnvironment env;
    private OrtSession session;

    // ─────────────────────────────────────────
    // 启动时加载模型
    // ─────────────────────────────────────────


    @PostConstruct
    public void init() throws Exception{


        Path tokenizerPath = Paths.get(
                getClass().getClassLoader()
                        .getResource("models/tokenizer.json")
                        .toURI()
        );

        Path modelPath = Paths.get(
                getClass().getClassLoader()
                        .getResource("models/model.onnx")
                        .toURI()
        );

        tokenizer = HuggingFaceTokenizer.newInstance(tokenizerPath);
        //tokenizer.json 里面其实存了一整个字典 + 规则。
        // 读取 tokenizer.json
        //↓
        //加载词典 vocabulary
        //↓
        //创建 tokenizer 对象


        env = OrtEnvironment.getEnvironment();
//        Creates the ONNX runtime environment.
//        ONNX Runtime is the engine that runs the AI model.
//        OrtEnvironment.getEnvironment() -> 启动 ONNX Runtime 环境


        session = env.createSession(
                modelPath.toString(),
                new OrtSession.SessionOptions()
        );
//        createSession -> 加载模型

//         modelpath in the previous code is to get the model.onnx path ex: /app/resources/models/model.onnx
//         but the createSession() need string so use
//         modelPath.toString() convert the path to string "/app/resources/models/model.onnx"

//        new OrtSession.SessionOptions() is the model operating way the current one is default method
//        which mean it use cpu to run it rather than use GPU

//        Flow of the code

//        ONNX Runtime
//        ↓
//        读取 model.onnx
//        ↓
//        解析神经网络
//        ↓
//        加载模型权重
//        ↓
//        创建 session

//        session = AI model instance

        System.out.println("✅ Local Embedding Model Loaded!");

    }

    // ─────────────────────────────────────────
    // 文字 → 向量
    // ─────────────────────────────────────────
    public double[] getEmbedding(String text) throws Exception {

        Encoding encoding    = tokenizer.encode(text);
//        This converts text → tokens.

//        Text:
//        "Plants produce glucose"

//        Tokenizer output:
//        ["plants", "produce", "glucose"]


        long[] inputIds      = encoding.getIds();
//        Example:
//        plants → 3452
//        produce → 7821
//        glucose → 9182
//
//        Result:
//        [3452, 7821, 9182]


        long[] attentionMask = encoding.getAttentionMask();

//        Attention mask 用来告诉模型：
//
//        哪些 token 是真实词
//        哪些是 padding = 补空位，让长度一样
//            1 = 有效 token
//            0 = padding
//
//        Example:
//
//        tokens: [3452, 7821, 9182]
//        mask:   [1,    1,    1]
//
//        如果 padding：
//
//        tokens: [3452, 7821, 9182, 0, 0]
//        mask:   [1,    1,    1,    0, 0]


        long[] tokenTypeIds  = encoding.getTypeIds();
//        tokenTypeIds 用来区分：
//
//        句子A
//        句子B

        long[] shape = {1, inputIds.length};
//        [batch_size, sequence_length]
//        例如：
//
//        1 sentence
//        3 tokens
//
//        shape：
//        [1,3]



        Map<String, OnnxTensor> inputs = new HashMap<>();
//        HashMap = Java里的字典

        inputs.put("input_ids",
                    OnnxTensor.createTensor(env, LongBuffer.wrap(inputIds), shape));
//        因为 AI 模型只能接受 tensor格式数据
//        Tensor = AI模型用的数据格式 / the data format that use for ai model
//        类似：
//
//        [3452, 7821, 9182]
//
//        变成：
//
//        Tensor([3452,7821,9182])
//
//        env -> Tensor must be created inside ONNX Runtime environment
//        ONNX doesn't use normal arrays.
//
        //It uses:
        //
        //Buffer (low-level memory)
        //
        //So we convert:
//
        //long[] → LongBuffer
        //
        // why need shape
        //
        // AI model needs table structure, not just a row.
        //1D:  [1,2,3]        ← just a row (no meaning)
        //2D:  [[1,2,3]]      ← 1 row, 3 columns
        //
        //[
        //  [3452, 7821, 9182]
        //]
        //
        // Java:
        //-----
        //inputIds → [3452, 7821, 9182]
        //
        //        ↓
        //
        //ONNX Runtime (C++):
        //-------------------
        //allocate memory
        //store: [3452, 7821, 9182]
        //store: shape [1,3]
        //
        //        ↓
        //
        //Tensor ready for model


        inputs.put("attention_mask",
                OnnxTensor.createTensor(env, LongBuffer.wrap(attentionMask), shape));
        inputs.put("token_type_ids",
                OnnxTensor.createTensor(env, LongBuffer.wrap(tokenTypeIds), shape));

        OrtSession.Result result = session.run(inputs);
//        OrtSession.Result is NOT your actual data.
//
//👉         It is a container (box) that holds model outputs.
//
//          Think:
//
//          result = {
//                  output_0: Tensor,
//                  output_1: Tensor,
//                ...
//              }

//        What happens internally:
//        ONNX receives:
//          input_ids
//          attention_mask
//          token_type_ids
//
//        Model processes through layers:
//          Embedding layer
//          Transformer layers (attention)
//          Hidden representations
//
//        Produces output tensor


        float[][][] output = (float[][][]) result.get(0).getValue();
//        [batch_size][sequence_length][embedding_dim] [句子][token][向量]
//        Example:
//
//          Input:
//
//          "Plants produce glucose"
//
//          Token count = 3
//          Embedding size = 4 (simplified)
//
//          Output:
//          [
//              [   ← batch (1 sentence)
//                  [0.1, 0.2, 0.3, 0.4],   ← token 1
//                  [0.5, 0.6, 0.7, 0.8],   ← token 2
//                  [0.9, 1.0, 1.1, 1.2]    ← token 3
//               ]
//          ]

        return normalize(meanPooling(output[0], attentionMask));
    }


    // ─────────────────────────────────────────
    // Mean Pooling
    // ─────────────────────────────────────────
    private double[] meanPooling(
            float[][] tokenEmbeddings,
//            float [number_of_tokens][embedding_size] / [token数量][向量长度]
            long[] attentionMask
//                    Example:  [1, 1, 0] 1 = real word, 0 = padding
    ) {

        int size = tokenEmbeddings[0].length;
        double[] pooled = new double[size];
//        create result vector -> This will store final sentence embedding
        double maskSum = 0;

        for (int i = 0; i < tokenEmbeddings.length; i++) { //Loop through tokens
            double mask = attentionMask[i]; //Read mask
            maskSum += mask;
            for (int j = 0; j < size; j++) { //Accumulate vectors
                pooled[j] += tokenEmbeddings[i][j] * mask;
            }
        }

        for (int j = 0; j < size; j++) {
            pooled[j] /= maskSum;
        }

        return pooled;
    }

    // ─────────────────────────────────────────
    // Normalize
    // ─────────────────────────────────────────
    private double[] normalize(double[] vector) {

        double norm = 0.0;
        for (double v : vector) norm += v * v;
        norm = Math.sqrt(norm);

        double[] normalized = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = vector[i] / norm;
        }
        return normalized;
    }

    // ─────────────────────────────────────────
    // Cosine Similarity
    // ─────────────────────────────────────────
    public double cosineSimilarity(double[] vectorA, double[] vectorB) {

        if (vectorA.length == 0 || vectorB.length == 0) return 0.0;

        double dot = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dot += vectorA[i] * vectorB[i];
        }
        return dot;
    }


}
