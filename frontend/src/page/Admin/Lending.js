import DefaultButton from '../../component/common/DefaultButton';
import { useState, useEffect } from 'react';
import { getUserByEmailApi } from '../../api/MemberApi';
import {
  getBookByCallSignApi,
  lendingBookApi,
  returningBookApi,
  getLendgingInfoApi,
} from '../../api/BookApi';
import DefaultSearchBar from '../../component/common/DefaultSearchBar';

export default function Lending() {
  const [searchEmail, setSearchEmail] = useState('');
  const [searchCallSign, setSearchCallSign] = useState('');
  const [condition, setCondition] = useState('');
  const [user, setUser] = useState({});
  const [book, setBook] = useState({});
  const [returningAt, setReturningAt] = useState('');
  const [transactionType, setTransactionType] = useState('lending'); // 'lending' 혹은 'returning'
  const [lendingInfo, setLendingInfo] = useState({});

  const handleGetUser = () => {
    getUserByEmailApi(searchEmail)
      .then(res => {
        setUser(res.data);
      })
      .catch(err => {
        setBook({});
        setUser({});
        alert(err.response.data.message);
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
      })
      .catch(err => {
        setBook({});
        setUser({});
        setReturningAt('');
        alert(err.response.data.message);
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

  const handleGetLendingInfo = () => {
    getLendgingInfoApi(searchCallSign)
      .then(res => {
        setLendingInfo(res.data);
        console.log(res);
      })
      .catch(err => {
        setBook({});
        setUser({});
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
    lendingBookApi(book.id, user.id, condition)
      .then(res => {
        alert(res.message);
        window.location.reload();
      })
      .catch(err => {
        alert(err.response.data.message);
        window.location.reload();
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
    // 현재 날짜와 반납 예정일을 Date 객체로 변환합니다.
    const now = new Date();
    const dueDate = new Date(lendingInfo.createdAt);
    dueDate.setDate(dueDate.getDate() + 14); // createdAt에서 14일을 더합니다.
    let diffDays = 0;

    // 두 날짜를 비교하여 연체 일수를 계산합니다.
    if (now > dueDate) {
      const diffTime = now - dueDate;
      diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    }

    // 연체 일수를 alert 창에 보여줍니다.
    if (
      window.confirm(
        `반납 예정일로부터 ${diffDays}일이 지났습니다. 반납하시겠습니까?`
      )
    ) {
      returningBookApi(lendingInfo.lendingId, condition)
        .then(res => {
          alert(res.message);
          window.location.reload();
        })
        .catch(err => {
          alert(err.response.data.message);
          let errors = err.response.data.errors;
          if (!errors) {
            return;
          }
          let errorMessages = errors
            .map((error, index) => `${index + 1}. ${error.message}`)
            .join('\n\n');
          alert(errorMessages);
        });
    }
  };

  useEffect(() => {
    setSearchEmail('');
    setSearchCallSign('');
    setCondition('');
    setUser({});
    setBook({});
    setReturningAt('');
  }, [transactionType]);

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
      {transactionType === 'lending' ? (
        <div className="flex justify-center items-center my-10 mx-48">
          <h1 className="text-2xl font-bold mr-6">유저 검색</h1>
          <DefaultSearchBar
            text="이메일을 입력해 주세요."
            value={searchEmail}
            setValue={setSearchEmail}
            handler={handleGetUser}
          ></DefaultSearchBar>
        </div>
      ) : (
        ''
      )}

      <div className="flex justify-center items-center my-10 sm:mx-32 xl:mx-80 border-2 border-gray-900">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                이메일 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {transactionType == 'lending'
                  ? user.email
                    ? user.email
                    : '-'
                  : lendingInfo.userEmail
                  ? lendingInfo.userEmail
                  : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                대출 수 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {transactionType == 'lending'
                  ? user.lendingCount
                    ? user.lendingCount + ' / 3 권'
                    : '-'
                  : lendingInfo.lendingCount
                  ? lendingInfo.lendingCount + ' / 3 권'
                  : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                연체현황 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {transactionType == 'lending'
                  ? user.penaltyEndDate
                    ? user.penaltyEndDate.split('T')[0]
                    : '-'
                  : lendingInfo.userPenaltyEndDate
                  ? lendingInfo.userPenaltyEndDate.split('T')[0]
                  : '-'}
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
          handler={
            transactionType === 'lending' ? handleGetBook : handleGetLendingInfo
          }
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
                {transactionType == 'lending'
                  ? book.bookInfo && book.bookInfo.title
                    ? book.bookInfo.title
                    : '-'
                  : lendingInfo.bookTitle
                  ? lendingInfo.bookTitle
                  : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                청구기호 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {transactionType === 'lending'
                  ? book.callSign
                    ? book.callSign
                    : '-'
                  : lendingInfo.bookCallSign
                  ? lendingInfo.bookCallSign
                  : '-'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                반납 예정일 :
              </dt>
              <dd className="mt-1 text-lg leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {transactionType === 'lending'
                  ? returningAt
                    ? returningAt
                    : '-'
                  : lendingInfo.createdAt
                  ? new Date(
                      new Date(lendingInfo.createdAt.split('T')[0]).getTime() +
                        14 * 24 * 60 * 60 * 1000 -
                        24 * 60 * 60 * 1000 // 하루를 뺍니다
                    )
                      .toISOString()
                      .split('T')[0]
                  : '-'}
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
          value={condition}
          onChange={event => setCondition(event.target.value)}
        ></textarea>
      </div>
      {transactionType === 'lending' && (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton color="blue" size="large" click={handleLendingBook}>
            대출 신청
          </DefaultButton>
        </div>
      )}

      {transactionType === 'returning' && (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton color="blue" size="large" click={handleReturningBook}>
            반납 신청
          </DefaultButton>
        </div>
      )}
    </main>
  );
}
