let currentQuestionIndex = 0;
const totalQuestions = 12;
const answers = new Array(totalQuestions).fill(null);

// 1. 선택한 답 저장
function selectAnswer(answer) {
  console.log("질문번호: ", currentQuestionIndex, " 응답: ", answer);
  // 현재 질문의 선택한 답을 배열에 저장
  answers[currentQuestionIndex] = answer;
  console.log("답 배열: ", JSON.stringify(answers));
  document.getElementById('answers').value = JSON.stringify(answers);
  nextQuestion(); // 다음 질문으로 넘어가기
}

// 2. 다음 질문 넘어가기
function nextQuestion() {
  const questions = document.querySelectorAll('.question');

  // 현재 질문 인덱스 != 마지막 질문
  if (currentQuestionIndex < questions.length - 1) {
    questions[currentQuestionIndex].classList.remove('active'); // 현재 질문 숨기기
    currentQuestionIndex++;
    questions[currentQuestionIndex].classList.add('active'); // 다음 질문 보이기
    document.getElementById('currentQuestion').value = currentQuestionIndex; // 인덱스 증가
    // 4. 진행바 업데이트 함수
    updateProgressBar(currentQuestionIndex);
  }
  // 마지막 질문일 때
  else {
    for (let i = 0; i < totalQuestions; i++) {
      if (!answers[i]) {
        answers[i] = 0;
      }
    }
    document.getElementById('answers').value = JSON.stringify(answers);
    // 제출
    document.getElementById('questionForm').submit();
  }
}

// 3. 뒤로 가기
function goBack() {
  const questions = document.querySelectorAll('.question');
  if (currentQuestionIndex > 0) {
    questions[currentQuestionIndex].classList.remove('active'); // 현재 질문 숨기기
    currentQuestionIndex--;
    questions[currentQuestionIndex].classList.add('active'); // 이전 질문 보이기
    document.getElementById('currentQuestion').value = currentQuestionIndex; // 인덱스 감소
    // 4. 진행바 업데이트 함수
    updateProgressBar(currentQuestionIndex);
  }
}

// 4. 진행바 업데이트 함수
function updateProgressBar(index) {
  const progressBar = document.querySelector('#progress-bar');
  const progressText = document.querySelector('#progress-text');
  const progressPercentage = ((index) / totalQuestions) * 100;
  progressBar.setAttribute('style', 'width: ' + progressPercentage + '%;');
  progressText.textContent = (index) + '/' + totalQuestions;
}

document.addEventListener("DOMContentLoaded", function () {
  updateProgressBar(currentQuestionIndex);
});

