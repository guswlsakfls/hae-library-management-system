import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
// import Login from './page/Login/Login';
import Main from './page/Main/Main';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/main" element={<Main />} />
        {/* <Route path="/" element={<Login />} /> */}
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
