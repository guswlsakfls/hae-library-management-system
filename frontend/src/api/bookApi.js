import axios from 'axios';

const serverIp = 'http://localhost:8080/api';

// freeBoard 해당 페이지 게시판 리스트 받아오기.
const getBookList = async (search, page, size) => {
  const res = await axios.get(serverIp + '/bookinfo/all', {
    params: {
      searchKey: search,
      page: page === null ? 1 : page,
      size: size === null ? 10 : size,
    },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

export { getBookList };
