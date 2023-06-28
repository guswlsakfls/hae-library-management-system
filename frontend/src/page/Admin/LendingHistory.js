import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';

const TableRow = ({
  email,
  name,
  bookTitle,
  lendingDate,
  returningDate,
  extension,
  status,
}) => (
  <tr>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {email}
    </td>
    <td className="py-4 whitespace-nowrap">
      <div className="px-1 flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{name}</div>
          <div className="text-sm text-gray-500">{bookTitle}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{lendingDate}</div>
          <div className="text-sm font-medium text-gray-500">{name}</div>
          <div className="text-sm text-gray-500">{bookTitle}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">
            {returningDate}
          </div>
          <div className="text-sm font-medium text-gray-500">{name}</div>
          <div className="text-sm text-gray-500">{bookTitle}</div>
        </div>
      </div>
    </td>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {extension}
    </td>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {status}
    </td>
  </tr>
);

const loans = [
  {
    email: 'guswlsakfls@gmail.com',
    name: 'Jane Cooper',
    bookTitle: 'Regional Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '1회',
    status: '대출중',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  {
    email: 'guswlsakfls@gmail.com',
    name: 'John Doe',
    bookTitle: 'Paradigm Technician',
    lendingDate: '2012-20-21',
    returningDate: '2012-20-23',
    extension: '2회',
    status: '대출완료',
  },
  // 기타 대출 정보...
];

export default function LendingHistory() {
  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">대출/반납 기록</h1>
      </div>
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
              연장
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
          {loans.map((loan, index) => (
            <TableRow
              key={index}
              email={loan.email}
              name={loan.name}
              bookTitle={loan.bookTitle}
              lendingDate={loan.lendingDate}
              returningDate={loan.returningDate}
              extension={loan.extension}
              status={loan.status}
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
