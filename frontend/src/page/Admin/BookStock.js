import React, { useState } from 'react';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import DefaultButton from '../../component/common/DefaultButton';

const books = [
  {
    title: '책 제목',
    callSign: '청구기호',
    lendingStatus: '대출중',
    status: '양호',
    createAt: '2021-10-10',
    updateAt: '2021-10-10',
  },
];

export default function BookStock() {
  const [isOpen, setIsOpen] = useState(false);

  function toggleModal() {
    setIsOpen(!isOpen);
  }

  const TableRow = ({
    title,
    callSign,
    lendingStatus,
    status,
    createAt,
    updateAt,
  }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {title}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {callSign}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {lendingStatus}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {status}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {createAt}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {updateAt}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={toggleModal}>
          삭제
        </DefaultButton>
      </td>
    </tr>
  );

  return (
    <>
      <main className="flex-grow h-screen overflow-y-scroll">
        <div className="flex justify-center items-center my-10 mx-48">
          <h1 className="text-4xl font-bold">도서 관리</h1>
        </div>
        <div className="flex justify-between items-center my-10 mx-48">
          <h1 className="text-2xl font-bold">보유 도서 검색</h1>
          <SearchBar text="청구기호를 입력해 주세요."></SearchBar>
          <div className="flex">
            <div className="mr-2">
              <Dropdown option1="대출일 순"></Dropdown>
            </div>
            <div className="mr-2">
              <Dropdown option1="대출중"></Dropdown>
            </div>
          </div>
        </div>
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                제목
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                청구기호
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                대출여부
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                상태
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                등록일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                수정일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              ></th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {books.map((book, index) => (
              <TableRow
                key={index}
                title={book.title}
                callSign={book.callSign}
                lendingStatus={book.lendingStatus}
                status={book.status}
                createAt={book.createAt}
                updateAt={book.updateAt}
              />
            ))}
          </tbody>
          <tfoot className="bg-white divide-y divide-gray-200">
            <tr>
              <td colSpan={6}>
                <div className="flex justify-center py-3">
                  <Pagination />
                </div>
              </td>
            </tr>
          </tfoot>
        </table>
      </main>
      {isOpen && (
        <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center z-50 bg-gray-500 bg-opacity-50">
          <div
            className="bg-white rounded-lg shadow-xl p-6 border border-gray-200 dark:border-gray-500"
            style={{
              width: 'fit-content',
              maxWidth: '80vw',
              height: 'fit-content',
              maxHeight: '80vh',
              overflow: 'auto',
            }}
          >
            {/* 모달 내용 */}
            <div className="flex justify-center items-center my-1 mx-48">
              <h1 className="text-2xl font-bold">도서 수정</h1>
            </div>
            <div className="flex flex-col">
              <div className="flex flex-row justify-between">
                <div className="flex flex-col">
                  <label
                    htmlFor="title"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    제목
                  </label>
                  <input
                    type="text"
                    name="title"
                    id="title"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="제목을 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />
                  <label
                    htmlFor="author"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    저자
                  </label>
                  <input
                    type="text"
                    name="author"
                    id="author"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="저자를 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />
                  <label
                    htmlFor="publisher"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    출판사
                  </label>
                  <input
                    type="text"
                    name="publisher"
                    id="publisher"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="출판사를 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />
                  <label
                    htmlFor="category"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    카테고리
                  </label>
                  <input
                    type="text"
                    name="category"
                    id="category"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="카테고리를 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />
                  <label
                    htmlFor="isbn"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    ISBN
                  </label>
                  <input
                    type="text"
                    name="isbn"
                    id="isbn"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="isbn을 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />
                  <label
                    htmlFor="callSign"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    청구기호
                  </label>
                  <input
                    type="text"
                    name="callSign"
                    id="callSign"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96"
                    placeholder="청구기호를 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />

                  <label
                    htmlFor="status"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    상태
                  </label>
                  <select
                    id="role"
                    name="role"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    // value={role}
                    // onChange={onChangeRole}
                  >
                    <option value="BOOK_FINE">보통</option>
                    <option value="BOOK_BREAK">파손</option>
                  </select>

                  <label
                    htmlFor="donator"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    기부자
                  </label>
                  <input
                    type="text"
                    name="donator"
                    id="donator"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    placeholder="기부자를 입력하세요."
                    // value={penalty}
                    // onChange={onChangePenalty}
                  />

                  <label
                    htmlFor="imageUrl"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    이미지 url
                  </label>
                  <input
                    type="text"
                    name="imageUrl"
                    id="imageUrl"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5"
                    placeholder="이미지 주소를 입력하세요."
                    // value={penalty}
                    // onChange={onChangePenalty}
                  />
                </div>
              </div>
              {/* 버튼 */}
              <div className="mt-4 flex justify-end">
                <button
                  type="button"
                  className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600"
                  onClick={toggleModal}
                >
                  취소하기
                </button>
                <button
                  type="button"
                  className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600"
                  onClick={toggleModal}
                >
                  확인
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
