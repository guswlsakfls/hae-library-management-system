import React from 'react';
import SearchBar from '../../component/common/SearchBar';
import Footer from '../../component/Footer';

function Main() {
  return (
    <>
      <div className="flex items-center justify-center flex-col h-full space-y-6">
        <div className="text-4xl font-bold">
          어서오세요, 현대오토에버 도서관입니다.
        </div>
        <div className="text-2xl">도서관에 오신 것을 환영합니다.</div>
        <SearchBar text="제목 또는 저자를 입력해주세요." />
      </div>
      <Footer />
    </>
  );
}

export default Main;
