import apiClient from "../Config/apiClient";
import { API_CONFIG } from "../Config/Base_url"

class QuizService{

    static BASE_URL = API_CONFIG.BASE_URL;

     static async createQuizRoom(token)
    {
        try{
            const response = await apiClient.post(`${this.BASE_URL}/user/create-quiz`,{} ,{
                headers:{Authorization: `Bearer ${token}`}
            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

      static async joinQuizRoom(token,roomCode)
    {
        try{
            const response = await apiClient.post(`${this.BASE_URL}/user/join-quiz/${roomCode}` ,{},{
                headers:{Authorization: `Bearer ${token}`}
            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

      static async getQuizRoomDetails(token,roomCode)
    {
        try{
            const response = await apiClient.get(`${this.BASE_URL}/user/quiz-session-detail/${roomCode}` ,{
                headers:{Authorization: `Bearer ${token}`}
            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

      static async startQuizRoom(token,roomCode)
    {
        try{
            const response = await apiClient.patch(`${this.BASE_URL}/user/start-quiz-session/${roomCode}` ,{},{
                headers:{Authorization: `Bearer ${token}`}
            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

       static async submitAnswer(token,roomCode,formData)
    {
        try{
            const response = await apiClient.post(`${this.BASE_URL}/user/submit-answer/${roomCode}`
                ,formData 
                ,{
                headers:{Authorization: `Bearer ${token}`}
                })

                // axios.post(url, data, config)
//          ↑    ↑      ↑
//        link  body  headers
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

         static async getLeaderboard(token,roomCode)
    {
        try{
            const response = await apiClient.get(`${this.BASE_URL}/user/get-leaderboard/${roomCode}`
                ,{
                headers:{Authorization: `Bearer ${token}`}
                }
            )

            return response.data;
        }catch(err)
        {
            throw err;
        }
    }


         static async getParticipantResult(token,roomCode)
    {
        try{
            const response = await apiClient.get(`${this.BASE_URL}/user/get-participant-result/${roomCode}`
                ,{
                headers:{Authorization: `Bearer ${token}`}
                }
            )

            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

    static async endQuizSession(token, roomCode) {
    try {
        const response = await apiClient.patch(
            `${this.BASE_URL}/user/end-quiz-session/${roomCode}`,{},
            {
                headers:{Authorization: `Bearer ${token}`}
                }
        );
        return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async rejoin(token, roomCode)
    {

        try{

            const respone = await apiClient.get(

               `${this.BASE_URL}/user/quiz-session/rejoin`,
            {
                params: { roomCode },
                headers: { Authorization: `Bearer ${token}` }
            }
                
            );
            return respone.data;
            
        }catch(err)
        {
            throw err;
        }

    }






}





export default QuizService;