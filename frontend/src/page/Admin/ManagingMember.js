import React, { useState, useEffect } from 'react';
import DefaultButton from '../../component/common/DefaultButton';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import { useSearchParams } from 'react-router-dom/dist';
import { getMemberListApi, updateMemberApi } from '../../api/MemberApi';
import 'react-datepicker/dist/react-datepicker.css';
import DatePicker from 'react-datepicker';

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
  const [modifyIsOpen, modifySetIsOpen] = useState(false);

  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [memberList, setMemberList] = useState([]);
  const [total, setTotal] = useState(0);
  // 수정할 책 정보를 관리하는 state 추가
  const [editMember, setEditMember] = useState({
    id: '',
    member: '',
    role: '',
    penaltyEndDate: '',
    activated: '',
  });
  const [startDate, setStartDate] = useState(new Date());

  const modifyToggleModal = () => {
    modifySetIsOpen(!modifyIsOpen);
  };

  // 수정 버튼 클릭 시 호출되는 함수
  const handleEditClick = member => {
    setEditMember(member);
    modifyToggleModal();
    console.log(member);
  };

  const checkToggleModal = () => {
    alert('삭제하시겠습니까?');
  };

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };
  const handleInputChange = event => {
    console.log(event.target.name, event.target.value);
    const { name } = event.target;
    let value = event.target.value; // 'let' 키워드 사용

    if (event.target.name === 'penaltyEndDate') {
      value.setHours(0, 0, 0, 0); // 시간 부분을 "00:00:00"으로 설정
      value.setMinutes(value.getMinutes() - value.getTimezoneOffset()); // 시간대를 UTC로 조정
      value = value.toISOString(); // ISO 8601 형식의 문자열로 변환
    }
    setEditMember(prevMember => {
      // 'bookInfo' 객체에 속한 속성인지 확인
      if (['email', 'role', 'penaltyEndDate', 'activated'].includes(name)) {
        return {
          ...prevMember,
          [name]: value,
        };
      }
    });
    console.log(editMember);
  };

  const onSubmit = e => {
    if (window.confirm('수정하시겠습니까?')) {
      updateMemberApi(editMember)
        .then(res => {
          console.log(res);
          alert(res.message);
          modifyToggleModal();
          window.location.reload();
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
        });
    }
  };

  useEffect(() => {
    getMemberListApi(search, page, size)
      .then(res => {
        setMemberList(res.data.memberList);
        setTotal(res.data.totalElements);
        setPage(res.data.currentPage);
        setSize(res.data.size);
        console.log(res);
      })
      .catch(err => {
        if (err.response.status === 401 || err.response.status === 403) {
          alert('로그인이 필요합니다.');
          window.location.href = '/login';
          return;
        }
        console.log(err.response);
        alert(err.response.data.message);
      });
  }, [search, page, size]);

  const TableRow = ({ member }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.email}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.role === 'ADMIN' ? '관리자' : '일반회원'}
      </td>

      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.penaltyEndDate
          ? new Date(member.penaltyEndDate).toISOString().split('T')[0]
          : '-'}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.lendingList && member.lendingList.length}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.createdAt}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {member.updatedAt}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={() => handleEditClick(member)}>
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
        <div className="flex justify-center items-center my-10 mx-96">
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
            {memberList.map((member, index) => (
              <TableRow key={index} member={member} />
            ))}
          </tbody>
          <tfoot className="bg-white divide-y divide-gray-200">
            <tr>
              <td colSpan={6}>
                <div className="flex justify-center py-3">
                  <Pagination
                    activePage={page + 1}
                    itemsCountPerPage={size}
                    totalItemsCount={(total / size) * 10}
                    pageRangeDisplayed={5}
                    prevPageText={'‹'}
                    nextPageText={'›'}
                    handlePageChange={handlePageChange}
                  ></Pagination>
                </div>
              </td>
            </tr>
          </tfoot>
        </table>
      </main>

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
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 mr-96 text-gray-500"
                    placeholder="이메일을 입력하세요."
                    value={editMember.email}
                    onChange={handleInputChange}
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
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 mr-96 text-gray-500"
                    value={editMember.role}
                    onChange={handleInputChange}
                  >
                    <option value="USER">일반 사용자</option>
                    <option value="ADMIN">관리자</option>
                  </select>

                  <label
                    htmlFor="role"
                    className="block text-sm font-medium text-gray-700"
                  >
                    계정 상태
                  </label>
                  <select
                    id="activated"
                    name="activated"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md text-gray-500"
                    value={editMember.activated}
                    onChange={handleInputChange}
                  >
                    <option value="true">사용 중</option>
                    <option value="false">휴면 계정</option>
                  </select>

                  <label
                    htmlFor="penaltyEndDate"
                    className="block text-sm font-medium text-gray-700 mt-5"
                  >
                    패널티
                  </label>
                  <DatePicker
                    id="penaltyEndDate"
                    selected={
                      editMember.penaltyEndDate
                        ? new Date(editMember.penaltyEndDate)
                        : null
                    }
                    onChange={date =>
                      handleInputChange({
                        target: { name: 'penaltyEndDate', value: date },
                      })
                    }
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 text-gray-500"
                    placeholderText="패널티를 입력하세요."
                  />
                </div>
              </div>
              {/* 버튼 */}
              <div className="mt-4 flex justify-end">
                <DefaultButton onClick={modifyToggleModal} color={'red'}>
                  취소
                </DefaultButton>

                <DefaultButton onClick={onSubmit} color={'blue'}>
                  수정
                </DefaultButton>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
