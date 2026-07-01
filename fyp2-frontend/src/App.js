import './App.css';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import NavBar from './Components/common/NavBar';
import Homepage from './Components/userpage/Home/Homepage';
import CreateQuiz from './Components/userpage/Quiz/Create_Quiz/Create_Quiz';
import QuizWaiting from './Components/userpage/Quiz/Quiz_waiting/Quiz_waiting';
import QuizAnswering from './Components/userpage/Quiz/Quiz_Answering/Quiz_Answering';
import QuizUserScore from './Components/userpage/Quiz/Quiz_user_score/Quiz_user_score';
import QuizLeaderboard from './Components/userpage/Quiz/Quiz_Leaderboard/Quiz_Leaderboard';
import QuizRoomCode from './Components/userpage/Quiz/Quiz_RoomCode/Quiz_RoomCode';
import SignUp from './Components/auth/SignUp/SignUp';
import Login from './Components/auth/Login/Login';
import UserList from './Components/adminpage/UserList/UserList';
import UpdateUser from './Components/adminpage/UpdateUser/UpdateUser';
import ViewUser from './Components/adminpage/ViewUser/ViewUser';
import CreateUser from './Components/adminpage/CreateUser/CreateUser';
import ManagedQuestionsPage from './Components/adminpage/ManagedQuestionsPage/ManageQuestionsPage';
import UpdateQuestion from './Components/adminpage/UpdateQuestion/UpdateQuestion';
import ViewQuestion from './Components/adminpage/ViewQuestion/ViewQuestion';
import RecoveryPasswordEmail from './Components/auth/RecoveryPassword/RecoveryPassword_Email/RecoveryPassword_Email';
import RecoveryPasswordOTP from './Components/auth/RecoveryPassword/RecoveryPassword_OTP/RecoveryPassword_OTP';
import RecoveryPasswordNewPassword from './Components/auth/RecoveryPassword/RecoveryPassword_NewPassword/RecoveryPassword_NewPassword';
import ProtectedRoute from './Components/auth/ProtectedRoute';
import { AuthProvider } from './Components/auth/AuthContext';
import AdminRoute from './Components/adminpage/AdminRoute ';
import Profile from './Components/userpage/Profile/profile';


function AppLayout() {
  const location = useLocation()

  const hideNavbarRoutes = [
    '/quiz_answering',
    '/quiz_leaderboard',
    '/recoveryPasswordEmail',
    '/recoveryPasswordOTP',
    '/recoveryPasswordNewPassword'
  ]

  const shouldHideNavbar = hideNavbarRoutes.includes(location.pathname)

  return (
    <>
      {!shouldHideNavbar && <NavBar />}
      <Routes>
        {/* Public routes (no auth needed) */}
        <Route path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/recoveryPasswordEmail" element={<RecoveryPasswordEmail />} />
        <Route path="/recoveryPasswordOTP" element={<RecoveryPasswordOTP />} />
        <Route path="/recoveryPasswordNewPassword" element={<RecoveryPasswordNewPassword />} />

        {/* Protected routes (must be logged in) */}
        <Route path="/create_quiz" element={<ProtectedRoute><CreateQuiz /></ProtectedRoute>} />
        <Route path="/quiz_waiting" element={<ProtectedRoute><QuizWaiting /></ProtectedRoute>} />
        <Route path="/quiz_answering" element={<ProtectedRoute><QuizAnswering /></ProtectedRoute>} />
        <Route path="/quiz_user_score" element={<ProtectedRoute><QuizUserScore /></ProtectedRoute>} />
        <Route path="/quiz_leaderboard" element={<ProtectedRoute><QuizLeaderboard /></ProtectedRoute>} />
        <Route path="/join_quiz" element={<ProtectedRoute><QuizRoomCode /></ProtectedRoute>} />
        <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />

        {/* Admin routes */}
        <Route path="/userList" element={<AdminRoute><UserList /></AdminRoute>} />
        <Route path="/updateUser" element={<AdminRoute><UpdateUser /></AdminRoute>} />
        <Route path="/viewUser" element={<AdminRoute><ViewUser /></AdminRoute>} />
        <Route path="/createUser" element={<AdminRoute><CreateUser /></AdminRoute>} />
        <Route path="/questionList" element={<AdminRoute><ManagedQuestionsPage /></AdminRoute>} />
        <Route path="/updateQuestion" element={<AdminRoute><UpdateQuestion /></AdminRoute>} />
        <Route path="/viewQuestion" element={<AdminRoute><ViewQuestion /></AdminRoute>} />
      </Routes>
    </>
  )
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppLayout />
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App;