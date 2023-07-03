import { useState } from 'react';
import Footer from '../../component/Footer';
import MyInfo from './MyInfo';
import MyLendingHistory from './MyLendingHistory';

/*
  This example requires some changes to your config:
  
  ```
  // tailwind.config.js
  module.exports = {
    // ...
    plugins: [
      // ...
      require('@tailwindcss/forms'),
    ],
  }
  ```
*/

export default function Mypage() {
  const [tab, setTab] = useState('userinfo');

  return (
    <>
      {/*
        This example requires updating your template:

        ```
        <html class="h-full bg-white">
        <body class="h-full">
        ```
      */}
      <div className="sm:mx-auto sm:w-full border-b-2">
        <h2 className="mt-10 pb-10 text-center text-4xl font-bold leading-9 tracking-tight text-gray-900">
          마이페이지
        </h2>
      </div>

      <div class="left-0 z-50 h-16 bg-white border-b mt-10 lg:mx-96">
        {/* ... */}
        <div class="grid h-full max-w-lg grid-cols-2 mx-auto font-medium justify-center">
          <button
            onClick={() => setTab('userinfo')}
            type="button"
            class={`inline-flex flex-col items-center justify-center px-5 hover:bg-gray-50 group ${
              tab === 'userinfo' ? 'text-blue-600' : 'text-gray-500'
            }`}
          >
            {/* SVG 코드 */}
            <span class="text-2xl  group-hover:text-blue-600 dark:group-hover:text-blue-500">
              내 정보
            </span>
          </button>
          <button
            onClick={() => setTab('lendingrecord')}
            type="button"
            class={`inline-flex flex-col items-center justify-center px-5 hover:bg-gray-50 group ${
              tab === 'lendingrecord' ? 'text-blue-600' : 'text-gray-500'
            }`}
          >
            {/* SVG 코드 */}
            <span class="text-2xl group-hover:text-blue-600 dark:group-hover:text-blue-500">
              대출 기록
            </span>
          </button>
        </div>
      </div>
      <div>{tab === 'userinfo' ? <MyInfo /> : <MyLendingHistory />}</div>

      <Footer />
    </>
  );
}
