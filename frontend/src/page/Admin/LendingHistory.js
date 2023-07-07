import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import { useState, useEffect } from 'react';
import { getLendingListApi } from '../../api/BookApi';
import { useSearchParams } from 'react-router-dom/dist';
import SelectBar from '../../component/common/SelectBar';

const TableRow = ({
  email,
  bookTitle,
  bookCallSign,
  lendingLibrarianEmail,
  lendingCondition,
  lendingDate,
  returningLibraryEmail,
  returningCondition,
  returningDate,
  status,
}) => (
  <tr>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {email}
    </td>
    <td className="py-4 whitespace-nowrap">
      <div className="px-1 flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{bookTitle}</div>
          <div className="text-sm text-gray-500">{bookCallSign}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{lendingDate}</div>
          <div className="text-sm font-medium text-gray-500">
            {lendingLibrarianEmail}
          </div>
          <div className="text-sm text-gray-500">{lendingCondition}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">
            {returningDate}
          </div>
          <div className="text-sm font-medium text-gray-500">
            {returningLibraryEmail}
          </div>
          <div className="text-sm text-gray-500">{returningCondition}</div>
        </div>
      </div>
    </td>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {status}
    </td>
  </tr>
);

// 대출 중, 반납완료
const isLendingOrReturningList = ['전체', '대출 중', '반납 완료'];

// 정렬 기준
const sortList = ['최신순', '오랜된순'];

export default function LendingHistory() {
  const [lendingList, setLendingList] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 2);
  const [total, setTotal] = useState(0);
  // 옵션 선택
  const [isLendingOrReturning, setIsLendingOrReturning] = useState('전체');
  const [sort, setSort] = useState('최신순');

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size, isLendingOrReturning, sort });
  };

  useEffect(() => {
    getLendingListApi(search, page, size, isLendingOrReturning, sort)
      .then(res => {
        setLendingList(res.data.lendingList);
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
  }, [search, page, size, isLendingOrReturning, sort]);

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">대출/반납 기록</h1>
      </div>
      <div className="flex justify-between items-center my-10 mx-48">
        <h1 className="text-2xl font-bold mr-2">대출/반납 검색</h1>
        <SearchBar
          text="이메일 또는 도서 제목으로 검색해 주세요."
          url="admin/lending-history"
        ></SearchBar>
        <div className="flex mx-4 mt-2">
          <SelectBar
            width="110"
            value={isLendingOrReturning}
            onChange={e => {
              setIsLendingOrReturning(e.target.value);
              setPage(0); // 카테고리 변경 시, 페이지를 1로 설정 (0-based index이기 때문에 0)
              setSearchParams({
                search,
                page: 1, // 카테고리 변경 시, 페이지를 1로 설정
                size,
                isLendingOrReturning: e.target.value,
                sort,
              });
            }}
            items={isLendingOrReturningList}
          ></SelectBar>
          <div className="mx-2">
            <SelectBar
              width="120"
              value={sort}
              onChange={e => {
                setSort(e.target.value);
                setPage(0); // 카테고리 변경 시, 페이지를 1로 설정 (0-based index이기 때문에 0)
                setSearchParams({
                  search,
                  page: 1, // 카테고리 변경 시, 페이지를 1로 설정
                  size,
                  isLendingOrReturning,
                  sort: e.target.value,
                });
              }}
              items={sortList}
            ></SelectBar>
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
              대출 신청자
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              도서정보
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
              상태
            </th>
            <th scope="col" className="relative px-6 py-3">
              <span className="sr-only">Edit</span>
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {lendingList.map((item, index) => (
            <TableRow
              key={index}
              email={item.userEmail}
              bookTitle={item.bookTitle}
              bookCallSign={item.bookCallSign}
              lendingLibrarianEmail={item.lendingLibrarianEmail}
              lendingCondition={item.lendingCondition}
              lendingDate={item.createdAt}
              returningLibraryEmail={item.returningLibrarianEmail}
              returningCondition={item.returningCondition}
              returningDate={item.returningAt}
              status={item.returningAt != null ? '반납완료' : '대출 중'}
            />
          ))}
        </tbody>
        <tfoot className="bg-white divide-y divide-gray-200">
          <tr>
            <td colSpan={6}>
              <div className="flex justify-center py-3">
                <div className="mt-3">
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
              </div>
            </td>
          </tr>
        </tfoot>
      </table>
    </main>
  );
}
