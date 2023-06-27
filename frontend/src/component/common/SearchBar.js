export default function SearchBar() {
  return (
    <div className="flex justify-center">
      <div className="relative rounded-md">
        <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
          <span className="text-gray-500 text-sm sm:text-base">$</span>
        </div>
        <input
          type="text"
          name="price"
          id="price"
          className="block w-96 h-10 rounded-md border-0 py-2 pl-8 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
          placeholder="제목 또는 ISBN으로 검색해주세요."
        />
      </div>
    </div>
  );
}
