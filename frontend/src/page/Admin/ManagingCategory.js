import { useEffect, useState } from 'react';
import {
  createCategoryApi,
  getCategoryListApi,
  updateCategoryApi,
  deleteCategoryApi,
} from '../../api/CategoryApi';

export default function ManagingCategory() {
  const [categories, setCategories] = useState(''); // 초기 카테고리
  const [inputValue, setInputValue] = useState(''); // 새 카테고리 입력 값

  useEffect(() => {
    // 카테고리를 로드하는 API 호출
    getCategoryListApi()
      .then(res => {
        setCategories(res.data);
      })
      .catch(err => {
        alert(err.message);
      });
  }, []);

  // 카테고리를 추가하는 함수
  const addCategory = () => {
    if (window.confirm('정말로 카테고리를 추가하시겠습니까?')) {
      createCategoryApi(inputValue)
        .then(res => {
          alert('카테고리를 성공적으로 추가했습니다.');
          window.location.reload();
        })
        .catch(err => {
          console.log(err.response);
          alert(err.response.data.message);

          let errors = err.response.data.errors;
          if (!errors) {
            return;
          }
          let errorMessages = errors
            .map((error, index) => `${index + 1}. ${error.message}`)
            .join('\n\n');
          alert(errorMessages);
        });
    }
  };

  // 카테고리를 수정하는 함수
  const editCategory = id => {
    const newCategoryName = prompt('새 카테고리명을 입력하세요.');
    if (newCategoryName !== null) {
      // 만약 취소 버튼이 눌리지 않았다면
      updateCategoryApi(id, newCategoryName)
        .then(res => {
          console.log(res);
          alert('카테고리를 성공적으로 수정했습니다.');
          window.location.reload();
        })
        .catch(err => {
          if (err.response) {
            alert(err.response.data.message);
          } else {
            alert('카테고리를 수정하는데 실패했습니다.');
          }
        });
    }
  };

  // 카테고리를 삭제하는 함수
  const deleteCategory = id => {
    if (window.confirm('정말로 카테고리를 삭제하시겠습니까?')) {
      deleteCategoryApi(id)
        .then(res => {
          alert('카테고리를 성공적으로 삭제했습니다.');
          window.location.reload();
        })
        .catch(err => {
          if (err.response) {
            alert(err.response.data.message + '\n삭제할 수 없습니다.');
          } else {
            alert('카테고리를 삭제하는데 실패했습니다.');
          }
        });
    }
  };

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex flex-col justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold mb-4">도서 카테고리 관리</h1>

        {/* 새 카테고리 입력 필드 및 추가 버튼 */}
        <div className="flex mb-4">
          <input
            type="text"
            value={inputValue}
            onChange={e => setInputValue(e.target.value)}
            placeholder="새 카테고리"
            className="mr-2 px-2 py-1 border border-gray-400 rounded"
          />
          <button
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            onClick={addCategory}
          >
            카테고리 추가
          </button>
        </div>

        {/* 카테고리 리스트 */}
        {categories &&
          categories.map((category, index) => (
            <div
              key={index}
              className="flex justify-between w-64 mb-2 border p-2 rounded"
            >
              <span>{category.categoryName}</span>
              <div>
                <button
                  className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-1 px-2 rounded mr-2"
                  onClick={() => editCategory(category.id)}
                >
                  수정
                </button>
                <button
                  className="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-2 rounded"
                  onClick={() => deleteCategory(category.id)}
                >
                  삭제
                </button>
              </div>
            </div>
          ))}
      </div>
    </main>
  );
}
