import { useState, useEffect } from 'react';
import queryString from 'query-string';
import { useNavigate } from 'react-router-dom';

export default function SearchBar(props) {
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const parsed = queryString.parse(window.location.search);
    if (parsed.search) {
      setSearchTerm(parsed.search);
    }
  }, []);

  const handleSubmit = event => {
    event.preventDefault();
    navigate(`/${props.url}?search=${searchTerm}&category=전체&sort=최신도서`);
  };

  return (
    <div className="flex justify-center">
      <form onSubmit={handleSubmit} className="relative rounded-md">
        <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            class="w-4 h-4"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
            />
          </svg>
        </div>
        <input
          type="text"
          name="book"
          id="book"
          className="block w-32 lg:w-80 h-10 rounded-md border-0 py-2 pl-8 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
          placeholder={props.text}
          value={searchTerm}
          onChange={e => setSearchTerm(e.target.value)}
        />
      </form>
    </div>
  );
}
