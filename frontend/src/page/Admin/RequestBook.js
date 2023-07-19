import React, { useState, useEffect } from 'react';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import DefaultButton from '../../component/common/DefaultButton';
import { useSearchParams } from 'react-router-dom/dist';
import {
  getBookInfoByIdApi,
  getRequestBookListApi,
  deleteRequestBookApi,
} from '../../api/BookApi';
import { getCategoryListApi } from '../../api/CategoryApi';
import SelectBar from '../../component/common/SelectBar';

// 이름순, 날짜순 정렬
const sortList = ['최신도서', '오래된도서'];

// 전체, 미승인, 구매완료
const approvedList = ['전체', '미승인', '구매완료'];

export default function ManagingBook() {
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [bookInfoList, setBookInfoList] = useState([]);
  const [total, setTotal] = useState(0);
  const [category, setCategory] = useState('전체');
  const [sort, setSort] = useState('최신도서');
  const [approved, setApproved] = useState('전체');
  const [categoryList, setCategoryList] = useState([]);

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size, category, approved, sort });
  };

  // 도서 삭제 신청
  const handleDeleteClick = id => {
    if (
      window.confirm(
        '[!주의!] 도서 관련 기록도 삭제됩니다. 정말 삭제하시겠습니까?'
      )
    ) {
      deleteRequestBookApi(id)
        .then(res => {
          alert(res.message);
          window.location.reload();
        })
        .catch(err => {
          alert(err.response.data.message);
        });
    }
  };

  useEffect(() => {
    getRequestBookListApi(search, page, size, category, sort, approved)
      .then(res => {
        setBookInfoList(res.data.requestBookInfoList);
        setTotal(res.data.totalElements);
        setPage(res.data.currentPage);
        setSize(res.data.size);
      })
      .catch(err => {
        if (err.response.status === 401 || err.response.status === 403) {
          alert('로그인이 필요합니다.');
          window.location.href = '/login';
          return;
        }
        alert(err.response.data.message);
      });

    // 카테고리 목록을 불러오는 함수
    getCategoryListApi()
      .then(res => {
        setCategoryList([{ value: 0, categoryName: '전체' }, ...res.data]);
      })
      .catch(err => {
        alert('카테고리 목록을 불러오는데 실패했습니다.');
      });
  }, [search, page, size, category, sort, approved]);

  const TableRow = ({ book }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.requestMemberEmail}
      </td>
      <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
        <div className="flex items-center">
          <div className="ml-4">
            <div className="text-sm font-medium text-gray-900">
              {book.title}
            </div>
            <div className="text-sm font-medium text-gray-500">{book.isbn}</div>
            <div className="text-sm text-gray-500">{book.categoryName}</div>
          </div>
        </div>
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.createdAt && book.createdAt.split('T')[0]}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.approvedAt ? book.approvedAt.split('T')[0] : '-'}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.approved ? '구매완료' : '미승인'}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton
          color={'red'}
          size="small"
          onClick={() => handleDeleteClick(book.id)}
        >
          삭제
        </DefaultButton>
      </td>
    </tr>
  );

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-18">
        <h1 className="text-4xl font-bold">도서 구매 목록</h1>
      </div>
      <div className="flex justify-between items-center my-10 mx-24">
        <h1 className="text-2xl font-bold">도서 검색</h1>
        <SearchBar
          text="요청자 또는 제목, ISBN로 검색해 주세요."
          url="admin/requestbook"
        ></SearchBar>
        <div className="flex mx-4 mt-2">
          <div className="mx-2">
            <SelectBar
              width="120"
              value={approved}
              onChange={e => {
                setApproved(e.target.value);
                setPage(0);
                setSearchParams({
                  search,
                  page: 1,
                  size,
                  approved: e.target.value,
                  category,
                  sort,
                });
              }}
              items={approvedList}
            ></SelectBar>
          </div>
          <SelectBar
            width="120"
            value={category}
            onChange={e => {
              setCategory(e.target.value);
              setPage(0); // 카테고리 변경 시, 페이지를 1로 설정 (0-based index이기 때문에 0)
              setSearchParams({
                search,
                page: 1, // 카테고리 변경 시, 페이지를 1로 설정
                size,
                approved,
                category: e.target.value,
                sort,
              });
            }}
            items={categoryList}
          ></SelectBar>
          <div className="mx-2">
            <SelectBar
              width="120"
              value={sort}
              onChange={e => {
                setSort(e.target.value);
                setPage(0);
                setSearchParams({
                  search,
                  page: 1,
                  size,
                  approved,
                  category,
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
              요청자
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
              등록일
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              구매일
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              구매 현황
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            ></th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {bookInfoList.length !== 0 ? (
            bookInfoList.map((book, index) => (
              <TableRow key={index} book={book} />
            ))
          ) : (
            <tr>
              <td className="text-center" colspan="5">
                검색한 도서가 존재하지 않습니다.
              </td>
            </tr>
          )}
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
  );
}
