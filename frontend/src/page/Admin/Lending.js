import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';

export default function Lending() {
  return (
    <main className="flex-grow">
      <div className="flex justify-between items-center my-10 mx-48">
        <h1 className="text-2xl font-bold">대출 신청</h1>
        <SearchBar></SearchBar>
        <div className="flex">
          <div className="mr-2">
            <Dropdown></Dropdown>
          </div>
          <div className="mr-2">
            <Dropdown></Dropdown>
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
              도서명
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
          <tr>
            <td className="px-6 py-4 whitespace-nowrap">
              <div className="flex items-center">
                <div className="flex-shrink-0 h-10 w-10">
                  <img
                    className="h-10 w-10 rounded-full"
                    src="https://images.unsplash.com/photo-1551963831-b3b1ca40c98e?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8YmVhdXR5JTIwY29sbGVjdGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80"
                    alt=""
                  />
                </div>
                <div className="ml-4">
                  <div className="text-sm font-medium text-gray-900">
                    Jane Cooper
                  </div>
                  <div className="text-sm text-gray-500"></div>
                </div>
              </div>
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              Regional Paradigm Technician
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              2012-20-21
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              1회
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              대출중
            </td>
          </tr>
        </tbody>
        <tbody className="bg-white divide-y divide-gray-200">
          <tr>
            <td className="px-6 py-4 whitespace-nowrap">
              <div className="flex items-center">
                <div className="flex-shrink-0 h-10 w-10">
                  <img
                    className="h-10 w-10 rounded-full"
                    src="https://images.unsplash.com/photo-1551963831-b3b1ca40c98e?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8YmVhdXR5JTIwY29sbGVjdGlvbnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80"
                    alt=""
                  />
                </div>
                <div className="ml-4">
                  <div className="text-sm font-medium text-gray-900">
                    Jane Cooper
                  </div>
                  <div className="text-sm text-gray-500"></div>
                </div>
              </div>
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              Regional Paradigm Technician
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              2012-20-21
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              1회
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              대출중
            </td>
          </tr>
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
