import DefaultButton from '../../component/common/DefaultButton';
import { useState, useEffect } from 'react';
import { getUserByEmailApi } from '../../api/MemberApi';
import {
  getBookByCallSignApi,
  lendingBookApi,
  returningBookApi,
} from '../../api/BookApi';
import DefaultSearchBar from '../../component/common/DefaultSearchBar';

export default function Lending() {
  const [searchEmail, serSearchEmail] = useState('');
  const [searchCallSign, setSearchCallSign] = useState('');
  const [lendingCondition, setLendingCondition] = useState('');
  const [user, setUser] = useState({});
  const [book, setBook] = useState({});
  const [returningAt, setReturningAt] = useState('');
  const [transactionType, setTransactionType] = useState('lending'); // 'lending' 혹은 'returning'

  const handleGetUser = () => {
    getUserByEmailApi(searchEmail)
      .then(res => {
        setUser(res.data);
        console.log(res);
      })
      .catch(err => {
        setUser({});
        alert(err.response.data.message);
        console.log(err.response.data);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
      });
  };

  const handleGetBook = () => {
    getBookByCallSignApi(searchCallSign)
      .then(res => {
        setBook(res.data);

        setReturningAt(
          (() => {
            // 현재 날짜를 가져옵니다.
            const now = new Date();

            // 현재 날짜에 2주를 더합니다. (1주는 7일이고 1일은 24*60*60*1000 밀리초입니다.)
            now.setDate(now.getDate() + 14);

            // 날짜를 YYYY-MM-DD 형식의 문자열로 변환합니다.
            const returnDate = now.toISOString().split('T')[0];

            return returnDate;
          })()
        );
        console.log(res);
      })
      .catch(err => {
        setBook({});
        setReturningAt('');
        alert(err.response.data.message);
        console.log(err.response.data);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
      });
  };

  const handleLendingBook = () => {
    lendingBookApi(book.id, user.id, lendingCondition)
      .then(res => {
        alert(res.message);
        setUser({});
        setBook({});
        setReturningAt('');
      })
      .catch(err => {
        alert(err.response.data.message);
        console.log(err.response.data);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
      });
  };
  const handleReturningBook = () => {
    returningBookApi(book.id, lendingCondition)
      .then(res => {
        alert(res.message);
        setUser({});
        setBook({});
        setReturningAt('');
      })
      .catch(err => {
        alert(err.response.data.message);
        console.log(err.response.data);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
      });
  };

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">대출/반납 신청</h1>
      </div>
      <div className="flex justify-center items-center my-10 mx-48 text-2xl">
        <select
          value={transactionType}
          onChange={e => setTransactionType(e.target.value)}
        >
          <option value="lending">대출</option>
          <option value="returning">반납</option>
        </select>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-2xl font-bold mr-6">유저 검색</h1>
        <DefaultSearchBar
          text="이메일을 입력해 주세요."
          value={searchEmail}
          setValue={serSearchEmail}
          handler={handleGetUser}
        ></DefaultSearchBar>
      </div>

      <div className="flex justify-center items-center my-10 sm:mx-32 xl:mx-80 border-2 border-gray-900">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                이메일 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.email ? user.email : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                대출 수 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.lendingList ? user.lendingList.length : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                연체현황 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.penaltyDate ? user.penaltyDate : '-'}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div className="flex justify-center items-center mt-24 mx-48">
        <h1 className="text-2xl font-bold mr-6">도서 검색</h1>
        <DefaultSearchBar
          text="청구기호를 입력해 주세요."
          value={searchCallSign}
          setValue={setSearchCallSign}
          handler={handleGetBook}
        ></DefaultSearchBar>
      </div>
      <div className="flex justify-center items-center my-10 sm:mx-32 xl:mx-80 border-2 border-gray-900">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                제목 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {book.bookInfo && book.bookInfo.title
                  ? book.bookInfo.title
                  : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                청구기호 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {book.callSign ? book.callSign : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                반납 예정일 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {returningAt ? returningAt : '-'}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <textarea
          id="returnMessage"
          name="returnMessage"
          rows="3"
          className="pl-1 border-t shadow-md focus:ring-indigo-500 focus:border-indigo-500 mt-1 block w-full sm:text-sm rounded-md"
          placeholder=" 특이사항을 적어주세요. (4~100자 이내)"
          value={lendingCondition}
          onChange={event => setLendingCondition(event.target.value)}
        ></textarea>
      </div>
      {transactionType === 'lending' && (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton size="large" click={handleLendingBook}>
            대출 신청
          </DefaultButton>
        </div>
      )}

      {transactionType === 'returning' && (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton size="large" click={handleReturningBook}>
            반납 신청
          </DefaultButton>
        </div>
      )}
    </main>
  );
}
