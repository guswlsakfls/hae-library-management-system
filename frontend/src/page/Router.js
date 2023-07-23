import { React, useEffect } from 'react';
import {
  BrowserRouter,
  Routes,
  Route,
  useLocation,
  useNavigate,
} from 'react-router-dom';
import Navbar from '../component/Navbar';
import Home from './Home/Home';
import Login from './Login/Login';
import Signup from './Login/Signup';
import BookList from './Book/BookList';
import BookInfo from './Book/BookInfo';
import NotFound from '../component/NotFound';
import LendingPage from './LendingPage';
import LendingHistoryPage from './LendingHistoryPage';
import AddBookPage from './AddBookPage';
import RequestBookPage from './RequestBookPage';
import ManagingBookPage from './ManagingBookPage';
import ManageingMemeberPage from './ManagingMemeberPage';
import ManagingCategoryPage from './ManagingCategoryPage';
import DefaultModal from '../component/DefaultModal';
import Mypage from './/Mypage/Mypage';
import jwt_decode from 'jwt-decode';

function AdminRoutes() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
      navigate('/', { state: { from: location } });
    } else {
      const decodedToken = jwt_decode(accessToken);
      if (decodedToken.ADMIN !== 'ROLE_ADMIN') {
        navigate('/', { state: { from: location } });
      }
    }
  }, [navigate, location]);

  return (
    // 관리자 페이지
    <Routes>
      <Route path="" element={<LendingPage />} />
      <Route path="lending" element={<LendingPage />} />
      <Route path="addbook" element={<AddBookPage />} />
      <Route path="requestbook" element={<RequestBookPage />} />
      <Route path="lending-history" element={<LendingHistoryPage />} />
      <Route path="book-stock" element={<ManagingBookPage />} />
      <Route path="category" element={<ManagingCategoryPage />} />
      <Route path="member" element={<ManageingMemeberPage />} />
      <Route path="modal" element={<DefaultModal />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

const Router = () => {
  return (
    <BrowserRouter>
      <div className="flex flex-col h-screen">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/booklist" element={<BookList />} />
          <Route path="/book/:id" element={<BookInfo />} />
          <Route path="/mypage/*" element={<Mypage />} />
          <Route path="/admin/*" element={<AdminRoutes />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default Router;
