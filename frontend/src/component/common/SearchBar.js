import { useState, useEffect } from 'react';
import queryString from 'query-string';

export default function SearchBar(props) {
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const parsed = queryString.parse(window.location.search);
    if (parsed.search) {
      setSearchTerm(parsed.search);
    }
  }, []);

  const handleSubmit = event => {
    event.preventDefault();
    window.location.href = `http://localhost:3000/booklist?search=${searchTerm}`;
  };

  return (
    <div className="flex justify-center">
      <form onSubmit={handleSubmit} className="relative rounded-md">
        <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
          <span className="text-gray-500 text-sm sm:text-base">$</span>
        </div>
        <input
          type="text"
          name="book"
          id="book"
          className="block w-96 h-10 rounded-md border-0 py-2 pl-8 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
          placeholder={props.text}
          value={searchTerm}
          onChange={e => setSearchTerm(e.target.value)}
        />
      </form>
    </div>
  );
}
