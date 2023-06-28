export default function DefaultButton({ children, size, click, ...rest }) {
  let buttonSizeClass = ''; // 버튼 크기를 나타내는 클래스를 저장할 변수

  // size 값에 따라 버튼 크기 클래스 지정
  switch (size) {
    case 'small':
      buttonSizeClass = 'py-1 px-2 text-sm';
      break;
    case 'large':
      buttonSizeClass = 'py-3 px-6 text-lg';
      break;
    default:
      buttonSizeClass = 'py-2 px-4 text-base';
      break;
  }

  return (
    <button
      className={`bg-blue-500 hover:bg-blue-700 text-white font-bold rounded m-2 ${buttonSizeClass}`}
      onClick={click}
      {...rest}
    >
      {children}
    </button>
  );
}
