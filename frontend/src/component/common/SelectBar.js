export default function SelectBar(props) {
  console.log(props.items);
  return (
    <div className="flex justify-center">
      <div className="relative w-40">
        <select
          value={props.value}
          onChange={props.onChange}
          className="block appearance-none w-full h-6 rounded-md border-0 pl-8 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
        >
          {props.items &&
            props.items.map((item, index) => (
              <option
                key={index}
                value={typeof item === 'object' ? item.categoryName : item}
              >
                {typeof item === 'object' ? item.categoryName : item}
              </option>
            ))}
        </select>
        <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
          <svg
            className="fill-current h-4 w-4"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 20 20"
          >
            <path d="M10 12l-5-6h10l-5 6z" />
          </svg>
        </div>
      </div>
    </div>
  );
}
