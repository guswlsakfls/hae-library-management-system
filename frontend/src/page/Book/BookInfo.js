import { useEffect, useState } from 'react';
import Footer from '../../component/Footer';
import { getBookInfoByIdApi } from '../../api/BookApi';

const statusText = {
  FINE: '양호',
  BREAK: '손상',
  LOST: '분실',
};

export default function BookInfo() {
  const [bookInfo, setBookInfo] = useState([]);

  const bookId = window.location.pathname.split('/')[2];

  useEffect(() => {
    getBookInfoByIdApi(bookId)
      .then(res => {
        setBookInfo(res.data);
        console.log(res.data);
      })
      .catch(err => {
        console.log(err.response);
        if (err.response.status === 401 || err.response.status === 403) {
          alert('로그인이 필요합니다.');
          window.location.href = '/login';
          return;
        }
        alert(err.response.data.message);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
        // window.location.href = '/error';
      });
  }, []);

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
          도서정보
        </h2>
      </div>
      <div className="flex flex-1 justify-center px-6 lg:px-8"></div>

      <div className="grid w-full grid-cols-1 items-start gap-x-6 gap-y-8 sm:grid-cols-12 lg:gap-x-8 px-32 pt-16">
        <div className="aspect-h-3 aspect-w-2 overflow-hidden rounded-lg bg-gray-100 sm:col-span-4 lg:col-span-5">
          <img
            src={bookInfo.image}
            alt={bookInfo.title}
            className="object-cover object-center"
          />
        </div>
        <div className="sm:col-span-8 lg:col-span-7">
          <h1 className="text-5xl font-bold text-gray-900 sm:pr-12 border-b pb-2">
            {bookInfo.title}
          </h1>

          <section aria-labelledby="options-heading" className="mt-5">
            {/* Colors */}
            <div className="px-4 py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                저자
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {bookInfo.author}
              </dd>
            </div>
            <div className="px-4 py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                출판사
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {bookInfo.publisher}
              </dd>
            </div>
            <div className="px-4 py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                발행연월
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {bookInfo.publishedAt}
              </dd>
            </div>
            <div className="px-4 py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                ISBN
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {bookInfo.isbn}
              </dd>
            </div>

            {/* Sizes */}
            <div className="mt-10">
              <div className="flex items-center justify-between border-b">
                <h4 className="text-2xl font-medium text-gray-900">보유도서</h4>
              </div>
              {/* 배열에 대한 .map() 함수 사용 */}
              {bookInfo.bookList === undefined
                ? null
                : bookInfo.bookList.map((book, index) => (
                    <div className="mt-6" key={index}>
                      <div className="flex items-center">
                        <h4 className="text-sm font-medium text-gray-900 mr-5">
                          {book.callSign} {/* 청구기호 */}
                        </h4>
                        <h4
                          className={`text-sm font-medium mr-5 ${
                            book.isAvailable === false
                              ? 'text-blue-500'
                              : 'text-red-500'
                          }`}
                        >
                          {book.isAvailable ? '대출 중' : '대출 가능'}{' '}
                          {/* 대출상태 */}
                        </h4>
                        <h4
                          className={`text-sm font-medium mr-5 ${
                            book.status === 'FINE'
                              ? 'text-blue-500'
                              : 'text-red-500'
                          }`}
                        >
                          {statusText[book.status]} {/* 대출상태 */}
                        </h4>
                      </div>
                    </div>
                  ))}
            </div>
          </section>
        </div>
      </div>
      <div className="justify-center flex">
        <button
          type="submit"
          className="mt-6 flex w-1/3 items-center justify-center rounded-md border border-transparent bg-blue-500 px-8 py-3 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
        >
          예약하기
        </button>
      </div>
      <Footer />
    </>
  );
}
