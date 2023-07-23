import { useState, useEffect } from 'react';
import Footer from '../../component/Footer';
import MyInfo from './MyInfo';
import MyLendingHistory from './MyLendingHistory';
import { useNavigate, useLocation } from 'react-router-dom';

export default function Mypage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [tab, setTab] = useState('userinfo');

  // TODO: 새로고침시 tab이 초기화되는 문제(내정보 불러오고, 대출기록 불러온다)
  // useEffect(() => {
  //   const currentPath = location.pathname.split('/').pop();
  //   setTab(currentPath);
  // }, [location]);

  const handleTabClick = newTab => {
    setTab(newTab);
    navigate(`/mypage/${newTab}`);
  };

  return (
    <>
      <div className="sm:mx-auto sm:w-full border-b-2">
        <h2 className="mt-10 pb-10 text-center text-4xl font-bold leading-9 tracking-tight text-gray-900">
          마이페이지
        </h2>
      </div>

      <div className="left-0 z-50 h-16 bg-white border-b mt-10 lg:mx-96">
        <div className="grid h-full max-w-lg grid-cols-2 mx-auto font-medium justify-center">
          <button
            onClick={() => handleTabClick('userinfo')}
            type="button"
            className={`inline-flex flex-col items-center justify-center px-5 hover:bg-gray-50 group ${
              tab === 'userinfo' ? 'text-blue-600' : 'text-gray-500'
            }`}
          >
            <span className="text-2xl group-hover:text-blue-600 dark:group-hover:text-blue-500">
              내 정보
            </span>
          </button>
          <button
            onClick={() => handleTabClick('my-lending-history')}
            type="button"
            className={`inline-flex flex-col items-center justify-center px-5 hover:bg-gray-50 group ${
              tab === 'my-lending-history' ? 'text-blue-600' : 'text-gray-500'
            }`}
          >
            <span className="text-2xl group-hover:text-blue-600 dark:group-hover:text-blue-500">
              대출 / 반납 기록
            </span>
          </button>
        </div>
      </div>
      <div>{tab === 'userinfo' ? <MyInfo /> : <MyLendingHistory />}</div>
      <Footer />
    </>
  );
}
