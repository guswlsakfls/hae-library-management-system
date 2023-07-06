export default function SelectBar(props) {
  console.log(props.items);
  let width = props.width ? props.width : '100'; // 기본값을 '40'으로 설정
  return (
    <div className="flex justify-center">
      <div className="relative" style={{ width: `${width}px` }}>
        <select
          value={props.value}
          onChange={props.onChange}
          className="block appearance-none w-full h-6 rounded-md border-0 pl-8 pr-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 hover:bg-blue-100 cursor-pointer"
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
      </div>
    </div>
  );
}
