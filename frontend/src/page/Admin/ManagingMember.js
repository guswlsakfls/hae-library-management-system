import React, { useState } from 'react';
import DefaultButton from '../../component/common/DefaultButton';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import MoInputBar from '../../component/common/MoInputBar';

const books = [
  {
    title: 'Regional Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    renew: '1회',
  },
];
const users = [
  {
    email: 'guswlsakfls@gmali.com',
    role: '관리자',
    penalty: '2021-10-10',
    lendingCount: 1,
    createAt: '2021-10-10',
    updateAt: '2021-10-10',
  },
];

export default function ManagingMember() {
  const [booksIsOpen, booksSetIsOpen] = useState(false);
  const [modifyIsOpen, modifySetIsOpen] = useState(false);
  const [checkIsOpen, checkSetIsOpen] = useState(false);

  function toggleModal() {
    booksSetIsOpen(!booksIsOpen);
  }

  function modifyToggleModal() {
    modifySetIsOpen(!modifyIsOpen);
  }

  function checkToggleModal() {
    checkSetIsOpen(!checkIsOpen);
  }

  const ModalTableRow = ({ title, lendingDate, returningDate, renew }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {title}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {lendingDate}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {returningDate}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {renew}
      </td>
    </tr>
  );

  const TableRow = ({
    email,
    role,
    penalty,
    lendingCount,
    createAt,
    updateAt,
  }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {email}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {role}
      </td>

      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {penalty}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {lendingCount}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {createAt}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {updateAt}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={toggleModal}>
          대출현황
        </DefaultButton>
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={modifyToggleModal}>
          수정
        </DefaultButton>
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={checkToggleModal}>
          삭제
        </DefaultButton>
      </td>
    </tr>
  );

  return (
    <>
      <main className="flex-grow h-screen overflow-y-scroll">
        <div className="flex justify-center items-center my-10 mx-48">
          <h1 className="text-4xl font-bold">회원 관리</h1>
        </div>
        <div className="flex justify-between items-center my-10 mx-48">
          <h1 className="text-2xl font-bold">회원 목록</h1>
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
                이메일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                권한
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                연체일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                대출권수
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                가입일
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
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              ></th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              ></th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {users.map((user, index) => (
              <TableRow
                key={index}
                email={user.email}
                role={user.role}
                penalty={user.penalty}
                lendingCount={user.lendingCount}
                createAt={user.createAt}
                updateAt={user.updateAt}
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
      {/* 대출 현황 모달 창 */}
      {booksIsOpen && (
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
            <div className="flex justify-center items-center my-5 mx-48">
              <h1 className="text-2xl font-bold">대출 현황</h1>
            </div>
            <div className="flex justify-center items-center my-5 mx-48">
              <h1 className="text-sm font-bold">이메일 적어야 한다.</h1>
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
                    대출일
                  </th>
                  <th
                    scope="col"
                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    반납일
                  </th>
                  <th
                    scope="col"
                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    연장
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {books.map((user, index) => (
                  <ModalTableRow
                    key={index}
                    title={user.title}
                    lendingDate={user.lendingDate}
                    returningDate={user.returningDate}
                    renew={user.renew}
                  />
                ))}
              </tbody>
            </table>

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
      )}

      {/*  멤버 수정 모달 창  */}
      {modifyIsOpen && (
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
            <div className="flex justify-center items-center my-5 mx-48">
              <h1 className="text-2xl font-bold">회원 수정</h1>
            </div>
            <div className="flex flex-col">
              <div className="flex flex-row justify-between">
                <div className="flex flex-col">
                  <label
                    htmlFor="email"
                    className="block text-sm font-medium text-gray-700 mt-10"
                  >
                    이메일
                  </label>
                  <input
                    type="text"
                    name="email"
                    id="email"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 mr-96"
                    placeholder="이메일을 입력하세요."
                    // value={email}
                    // onChange={onChangeEmail}
                  />

                  <label
                    htmlFor="role"
                    className="block text-sm font-medium text-gray-700"
                  >
                    권한
                  </label>
                  <select
                    id="role"
                    name="role"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    // value={role}
                    // onChange={onChangeRole}
                  >
                    <option value="ROLE_USER">일반 사용자</option>
                    <option value="ROLE_ADMIN">관리자</option>
                  </select>

                  <label
                    htmlFor="penalty"
                    className="block text-sm font-medium text-gray-700 mt-5"
                  >
                    패널티
                  </label>
                  <input
                    type="text"
                    name="penalty"
                    id="penalty"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5"
                    placeholder="패널티를 입력하세요."
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
                  onClick={modifyToggleModal}
                >
                  취소하기
                </button>
                <button
                  type="button"
                  className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600"
                  onClick={modifyToggleModal}
                >
                  확인
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
      {checkIsOpen && (
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
            <p className="text-base leading-relaxed text-black">
              정말로 하시겠습니까?
            </p>

            {/* 버튼 */}
            <div className="mt-4 flex justify-end">
              <button
                type="button"
                className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600"
                onClick={checkToggleModal}
              >
                취소하기
              </button>
              <button
                type="button"
                className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-gray-600"
                onClick={checkToggleModal}
              >
                확인
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
