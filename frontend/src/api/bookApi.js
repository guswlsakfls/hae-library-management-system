import axios from 'axios';

const serverIp = 'http://localhost:8080/api';

const postAddBook = async (
  title,
  image,
  author,
  publisher,
  publishedAt,
  isbn,
  category,
  callSign,
  donator,
  status
) => {
  const res = await axios.post(serverIp + '/book/create', {
    title: title,
    image: image,
    author: author,
    publisher: publisher,
    publishedAt: publishedAt,
    isbn: isbn,
    category: category,
    callSign: callSign,
    donator: donator,
    status: status,
  });
  return res.data;
};

// freeBoard 해당 페이지 게시판 리스트 받아오기.
const getBookList = async (search, page, size) => {
  console.log(page);
  const res = await axios.get(serverIp + '/bookinfo/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
    },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const getBookByCallSign = async callSign => {
  const res = await axios.get(serverIp + '/book/callsign', {
    params: {
      callsign: callSign,
    },
  });
  return res.data;
};

const getBookInfoById = async id => {
  const res = await axios.get(serverIp + '/bookinfo/' + id);
  return res.data;
};

const getAddBookByIsbn = async isbn => {
  const res = await axios.get(serverIp + '/bookinfo/isbn/' + isbn);
  return res.data;
};

const lendingBook = async (bookId, userId, lendingCondition) => {
  const res = await axios.post(serverIp + '/lending/create', {
    bookId: bookId,
    userId: userId,
    lendingCondition: lendingCondition,
  });
  return res.data;
};

export {
  getBookList,
  getBookInfoById,
  getBookByCallSign,
  getAddBookByIsbn,
  postAddBook,
  lendingBook,
};
