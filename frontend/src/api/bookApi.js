import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

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
  console.log('category:', category);
  const res = await axios.post(
    serverIp + '/admin/book/create',
    {
      title: title,
      image: image,
      author: author,
      publisher: publisher,
      publishedAt: publishedAt,
      isbn: isbn,
      categoryName: category,
      callSign: callSign,
      donator: donator,
      status: status,
    },
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

// freeBoard 해당 페이지 게시판 리스트 받아오기.
const getBookListApi = async (search, page, size, category, sort) => {
  console.log(search, page, size, category, sort);
  const res = await axios.get(serverIp + '/bookinfo/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 2 : size,
      category: category,
      sort: sort,
    },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const getLendingListApi = async (search, page, size) => {
  console.log(page);
  const res = await axios.get(serverIp + '/admin/lending/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const getBookByCallSignApi = async callSign => {
  const res = await axios.get(serverIp + '/admin/book/callsign', {
    params: {
      callsign: callSign,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const getBookInfoByIdApi = async id => {
  const res = await axios.get(serverIp + '/bookinfo/' + id);
  return res.data;
};

const addBookByIsbnApi = async isbn => {
  const res = await axios.get(serverIp + '/admin/bookinfo/isbn', {
    params: {
      isbn: isbn,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const lendingBookApi = async (bookId, userId, lendingCondition) => {
  const res = await axios.post(
    serverIp + '/admin/lending/create',
    {
      bookId: bookId,
      userId: userId,
      lendingCondition: lendingCondition,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const returningBookApi = async (lendingId, returningCondition) => {
  const res = await axios.put(
    serverIp + '/admin/lending/returning',
    {
      lendingId: lendingId,
      returningCondition: returningCondition,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getBookStockListApi = async (search, page, size) => {
  const res = await axios.get(serverIp + '/admin/book/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const updateBookStockApi = async editBook => {
  console.log(editBook);
  const res = await axios.put(
    serverIp + '/admin/book/update',
    {
      id: editBook.id,
      title: editBook.bookInfo.title,
      isbn: editBook.bookInfo.isbn,
      author: editBook.bookInfo.author,
      image: editBook.bookInfo.image,
      callSign: editBook.callSign,
      donator: editBook.donator,
      status: editBook.status,
      categoryName: editBook.bookInfo.categoryName,
      publisher: editBook.bookInfo.publisher,
      publishedAt: editBook.bookInfo.publishedAt,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getMeLendingHistoryListApi = async (search, page, size) => {
  const res = await axios.get(serverIp + '/member/lending-history/me', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const getLendgingInfoApi = async callSign => {
  const res = await axios.get(serverIp + '/admin/lending/callsign', {
    params: {
      callSign: callSign,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

export {
  getBookListApi,
  getBookInfoByIdApi,
  getBookByCallSignApi,
  addBookByIsbnApi,
  postAddBook,
  lendingBookApi,
  returningBookApi,
  getLendingListApi,
  getBookStockListApi,
  updateBookStockApi,
  getMeLendingHistoryListApi,
  getLendgingInfoApi,
};
