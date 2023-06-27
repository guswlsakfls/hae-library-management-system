import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from '../component/Navbar';
import Home from './Home/Home';
import Login from './Login/Login';
import Signup from './Login/Signup';
import BookList from './Book/BookList';
import BookInfo from './Book/BookInfo';
import Footer from '../component/Footer';
import Info from './Home/Info';
import NotFound from '../component/NotFound';
import LendingPage from './LendingPage';

const Router = () => {
  return (
    <BrowserRouter>
      <div className="flex flex-col h-screen">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          {/* <Route element={<LimitedRoute isLogoutOnly />}> */}
          <Route>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
          </Route>
          <Route path="/book" element={<BookList />} />
          <Route path="/info" element={<Info />} />
          <Route path="/book/:id" element={<BookInfo />} />
          {/* <Route element={<LimitedRoute isLoginOnly />}>
            <Route path="/mypage" element={<MyPageRoutes />}>
              <Route index element={<Mypage />} />
              <Route path="edit/:mode" element={<EditEmailOrPassword />} />
            </Route>
          </Route> */}
          {/* <Route element={<LimitedRoute isAdminOnly />}> */}
          <Route path="/Lending" element={<LendingPage />} />
          {/* <Route path="/return" element={<ReturnBook />} />
            <Route path="/reservation" element={<ReservedLoan />} />
            <Route path="/addbook" element={<AddBook />} />
            <Route path="/user" element={<UserManagement />} />
            <Route path="/history" element={<History />} />
            <Route path="/book" element={<BookManagement />} />
            <Route path="/review" element={<ReviewManagement />} />
            <Route path="/stock" element={<BookStock />} /> */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default Router;
