import Dropdown from '../../component/common/Dropdown';
import SearchBar from '../../component/common/SearchBar';
import Footer from '../../component/Footer';
import Pagination from '../../component/common/Pagination';
import { useState, useEffect } from 'react';
import { getBookList } from '../../api/BookApi';
import { useSearchParams } from 'react-router-dom/dist';

export default function BookList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 20);
  const [bookInfoList, setBookInfoList] = useState([]);
  const [total, setTotal] = useState(0);

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };

  useEffect(() => {
    getBookList(search, page, size)
      .then(res => {
        setBookInfoList(res.data.bookInfoList);
        setTotal(res.data.totalElements);
        setPage(res.data.currentPage);
        setSize(res.data.size);
        console.log(res);
      })
      .catch(err => {
        alert(err.response.data.message);
        window.location.href = '/error';
      });
  }, [search, page, size]);

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
          도서목록
        </h2>
      </div>
      <div className="flex justify-between items-center my-10 mx-72">
        <h1 className="text-2xl font-bold">도서 검색</h1>
        <SearchBar text="제목 또는 저자를 입력해 주세요."></SearchBar>
        <div className="flex">
          <div className="mr-2">
            <Dropdown option1="전체"></Dropdown>
          </div>
          <div className="mr-2">
            <Dropdown option1="이름순"></Dropdown>
          </div>
        </div>
      </div>
      {/* 리스트 */}
      <div className="flex flex-wrap justify-start lg:px-12 mx-32 border-t-2 border-b-2">
        {bookInfoList.map(book => (
          <div key={book.id} className="lg:w-1/2 w-full px-2">
            <li className="flex py-6 px-8">
              <div className="h-24 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
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
        ))}
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
