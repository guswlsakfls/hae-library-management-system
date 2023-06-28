import SearchBar from '../../component/common/SearchBar';
import DefaultButton from '../../component/common/DefaultButton';

const user = {
  email: 'guswlsakfls@gmail.com',
  lendingCount: 2,
  penaltyDate: '2021-10-10',
};

const book = {
  title: 'Regional Paradigm Technician',
  callSign: '123.12.v1.c1',
  returningDate: '2012-20-23',
};

export default function Lending() {
  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">대출/반납 신청</h1>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-2xl font-bold mr-6">유저 검색</h1>
        <SearchBar text="이메일을 입력해 주세요."></SearchBar>
      </div>

      <div className="flex justify-center items-center my-10 mx-80 border-2 border-gray-900">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                이메일
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.email}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                대출 수
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.lendingCount}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                연체현황
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {user.penaltyDate}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div className="flex justify-center items-center mt-24 mx-48">
        <h1 className="text-2xl font-bold mr-6">도서 검색</h1>
        <SearchBar text="청구기호를 입력해 주세요."></SearchBar>
      </div>
      <div className="flex justify-center items-center my-10 mx-80 border-2 border-gray-900">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                제목
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {book.title}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                청구기호
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {book.callSign}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                반납 예정일
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-700 sm:col-span-2 sm:mt-0">
                {book.returningDate}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <DefaultButton size="large">대출 신청</DefaultButton>
      </div>
    </main>
  );
}
