export default function InputBar(props) {
  return (
    <div className="flex justify-center">
      <div className="relative rounded-md">
        <input
          type="text"
          id="price"
          value={props.value} // value prop 추가
          className="block w-96 h-6 rounded-md border-0 py-2 pl-3 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
          onChange={props.onChange}
        />
      </div>
    </div>
  );
}
