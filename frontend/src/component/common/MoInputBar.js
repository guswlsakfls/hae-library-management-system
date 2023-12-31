export default function MoInputBar(props) {
  return (
    <div className="flex">
      <div className="relative rounded-md">
        <input
          type="text"
          name="price"
          id="price"
          className="block w-96 h-6 rounded-md border-0 py-2 text-base text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600"
        />
      </div>
    </div>
  );
}
