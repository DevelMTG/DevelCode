/*
  ## 실행 시점 
   1. HTML 파싱: 브라우저가 위에서 아래로 태그를 읽음
   2. DOM 구성 완료: document.ready 또는 DOMContentLoaded 이벤트 발생 시점
   3. ESM 실행 👈 바로 여기! (별도 설정 없이도 defer 속성을 가진 것처럼 동작함)
   4. Window 로드: 이미지, CSS 등이 모두 완료된 window.onload 시점
*/

// DOMContentLoaded 이벤트 생성 방법
document.addEventListener('DOMContentLoaded', () => {
  // DOM이 준비되었을 때 실행할 코드
  const btn = document.querySelector('#myButton');
  console.log('DOM 트리 완성!');
});
