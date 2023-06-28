import React from 'react';
import SearchBar from '../../component/common/SearchBar';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';

export default function MyLendingHistory() {
  const TableRow = ({
    bookTitle,
    callSign,
    lendingDate,
    returningDate,
    renew,
  }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {bookTitle}
      </td>
      <td className="py-4 whitespace-nowrap">
        <div className="px-1 flex items-center">
          <div className="ml-4">
            <div className="text-sm text-gray-500">{bookTitle}</div>
          </div>
        </div>
      </td>
      <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
        <div className="flex items-center">
          <div className="ml-4">
            <div className="text-sm font-medium text-gray-900">
              {lendingDate}
            </div>
          </div>
        </div>
      </td>
      <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
        <div className="flex items-center">
          <div className="ml-4">
            <div className="text-sm font-medium text-gray-900">
              {returningDate}
            </div>
          </div>
        </div>
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {renew}
      </td>
    </tr>
  );

  const books = [
    {
      bookTitle: 'Regional Paradigm Technician',
      callSign: '123.23.v1.c1',
      lendingDate: '2012-20-21',
      returningDate: '2012-20-23',
      renew: '1회',
    },
    {
      bookTitle: 'Regional Paradigm Technician',
      callSign: '123.23.v1.c1',
      lendingDate: '2012-20-21',
      returningDate: '2012-20-23',
      renew: '1회',
    },
    {
      bookTitle: 'Regional Paradigm Technician',
      callSign: '123.23.v1.c1',
      lendingDate: '2012-20-21',
      returningDate: '2012-20-23',
      renew: '1회',
    },
    {
      bookTitle: 'Regional Paradigm Technician',
      callSign: '123.23.v1.c1',
      lendingDate: '2012-20-21',
      returningDate: '2012-20-23',
      renew: '1회',
    },
  ];

  return (
    <main className="flex-grow h-screen mx-32">
      <div className="flex justify-between items-center my-10 mx-48">
        <h1 className="text-2xl font-bold">대출/반납 검색</h1>
        <SearchBar></SearchBar>
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
          {books.map((book, index) => (
            <TableRow
              key={index}
              bookTitle={book.bookTitle}
              callSign={book.callSign}
              lendingDate={book.lendingDate}
              returningDate={book.returningDate}
              renew={book.renew}
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
  );
}
