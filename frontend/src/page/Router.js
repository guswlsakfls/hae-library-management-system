import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from '../component/Navbar';
import Home from './Home/Home';
import Login from './Login/Login';
import Signup from './Login/Signup';
import BookList from './Book/BookList';
import BookInfo from './Book/BookInfo';
import Info from './Home/Info';
import NotFound from '../component/NotFound';
import LendingPage from './LendingPage';
import LendingHistoryPage from './LendingHistoryPage';
import AddBookPage from './AddBookPage';
import BookStockPage from './BookStockPage';
import ManageingMemeberPage from './ManagingMemeberPage';
import DefaultModal from '../component/DefaultModal';
import Mypage from './/Mypage/Mypage';

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
          {/* <Route element={<LimitedRoute isLoginOnly />}> */}
          <Route path="/mypage" element={<Mypage />} />
          {/* </Route> */}
          {/* <Route element={<LimitedRoute isAdminOnly />}> */}
          <Route path="/admin/*">
            <Route path="" element={<LendingPage />} />
            <Route path="lending" element={<LendingPage />} />{' '}
            <Route path="addbook" element={<AddBookPage />} />{' '}
            <Route path="lending-history" element={<LendingHistoryPage />} />
            <Route path="book-stock" element={<BookStockPage />} />
            <Route path="member" element={<ManageingMemeberPage />} />
            <Route path="modal" element={<DefaultModal />} />
          </Route>
          {/* <Route path="/reservation" element={<ReservedLoan />} /> */}
          {/* <Route path="/book" element={<BookManagement />} /> */}
          {/* <Route path="/review" element={<ReviewManagement />} /> */}
          {/* <Route path="/stock" element={<BookStock />} /> */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default Router;
