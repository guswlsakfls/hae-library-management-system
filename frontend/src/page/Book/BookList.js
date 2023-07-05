import SearchBar from '../../component/common/SearchBar';
import Footer from '../../component/Footer';
import Pagination from '../../component/common/Pagination';
import { useState, useEffect } from 'react';
import { getBookListApi } from '../../api/BookApi';
import { useSearchParams } from 'react-router-dom/dist';
import SelectBar from '../../component/common/SelectBar';
import { getCategoryListApi } from '../../api/CategoryApi';

// 이름순, 날짜순 정렬
const sortList = ['최신도서', '오래된도서'];

export default function BookList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 2);
  const [bookInfoList, setBookInfoList] = useState([]);
  const [total, setTotal] = useState(0);
  const [category, setCategory] = useState('전체');
  const [sort, setSort] = useState('최신도서');
  const [categoryList, setCategoryList] = useState([]);

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size, category, sort });
  };

  useEffect(() => {
    // 카테고리 목록을 불러오는 함수
    getCategoryListApi()
      .then(res => {
        console.log('selecti: ', res);
        setCategoryList([{ value: 0, categoryName: '전체' }, ...res.data]);
      })
      .catch(err => {
        console.log(err);
        alert('카테고리 목록을 불러오는데 실패했습니다.');
      });

    // 도서 목록을 불러오는 함수
    getBookListApi(search, page, size, category, sort)
      .then(res => {
        setBookInfoList(res.data.bookInfoList);
        setTotal(res.data.totalElements);
        setPage(res.data.currentPage);
        setSize(res.data.size);
        console.log(res);
      })
      .catch(err => {
        console.log(err);
        console.log(err.response);
        alert(err.response.data.message);
      });
  }, [search, page, size, category, sort]);

  return (
    <>
      <div className="sm:mx-auto sm:w-full border-b-2">
        <h2 className="mt-10 pb-10 text-center text-4xl font-bold leading-9 tracking-tight text-gray-900">
          도서목록
        </h2>
      </div>
      <div className="flex flex-wrap items-center my-10 mx-72">
        <h1 className="sm:text-sm lg:text-2xl font-bold mx-4">도서 검색</h1>
        <SearchBar
          text="제목 또는 저자를 입력해 주세요."
          url="booklist"
        ></SearchBar>
        <div className="flex mx-4 mt-2">
          <SelectBar
            value={category}
            onChange={e => {
              setCategory(e.target.value);
              setSearchParams({
                search,
                page,
                size,
                category: e.target.value,
                sort,
              });
            }}
            items={categoryList}
          ></SelectBar>
          <div className="mx-2">
            <SelectBar
              width="135"
              value={sort}
              onChange={e => {
                setSort(e.target.value);
                setSearchParams({
                  search,
                  page,
                  size,
                  category,
                  sort: e.target.value,
                });
              }}
              items={sortList}
            ></SelectBar>
          </div>
        </div>
      </div>
      {/* 리스트 */}
      <div className="flex flex-wrap justify-start lg:px-12 mx-32 border-t-2 border-b-2">
        {bookInfoList.length !== 0 ? (
          bookInfoList.map(book => (
            <div key={book.id} className="lg:w-1/2 w-full px-2">
              <li className="flex py-6 px-8">
                <div className="h-32 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                  <img
                    src={book.image}
                    alt={book.title}
                    className="h-full w-full object-cover object-center"
                  />
                </div>

                <div className="ml-4 flex flex-1 flex-col">
                  <div>
                    <div className="flex justify-between text-base font-medium text-gray-900">
                      <h3>{book.title}</h3>
                      <p className="ml-4">{book.category}</p>
                    </div>
                    <p className="mt-1 text-sm text-gray-500">{book.author}</p>
                  </div>
                  <div className="flex flex-1 items-end justify-between text-sm">
                    <p className="text-gray-500">{book.publisher}</p>

                    <div className="flex">
                      <button
                        type="button"
                        className="font-medium text-gray-400 hover:text-indigo-500"
                        onClick={() => {
                          window.location.href = `/book/${book.id}`;
                        }}
                      >
                        자세히 보기 >
                      </button>
                    </div>
                  </div>
                </div>
              </li>
            </div>
          ))
        ) : (
          <div className="flex justify-center items-center w-full h-32 text-4xl my-5">
            검색한 도서가 없습니다. 다시 검색해 주세요
          </div>
        )}
      </div>

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
      <Footer />
    </>
  );
}
