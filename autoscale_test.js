import http from 'k6/http';
import {sleep} from 'k6';

export const options = {
  scenarios: {
    contacts: {
      executor: 'ramping-arrival-rate',
      startRate: '200',
      timeUnit: '1s',
      preAllocatedVUs: 1200,
      stages: [
        {target: 400, duration: '30s'},
        {target: 800, duration: '30s'},
        {target: 1200, duration: '30s'},
        {target: 1400, duration: '30s'},
        {target: 1700, duration: '1m'},
      ],
    },
  },

};

export default function () {
  const url = 'http://mbtkids-lb-ec2-260733231.ap-northeast-2.elb.amazonaws.com:8080/mbtkids/events'; // 이벤트 응모 URL

  // 각 가상 사용자의 ID를 이용하여 이름과 전화번호 생성
  const userId = __VU; // 현재 가상 사용자 ID
  const name = `test${userId}`; // 이름 생성 (test1, test2, ...)
  const phone = `010-1234-${5678 + userId}`; // 전화번호 생성 (예: 010-1234-5679, 010-1234-5680, ...)

  const payload = `name=${name}&phone=${phone}`; // URL 인코딩된 형식으로 전송

  const params = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded', // 폼 데이터 형식
    },
  };

  // POST 요청을 통해 응모하기
  const res = http.post(url, payload, params);

  // 응답 상태 코드 로깅
  console.log(`응답 상태: ${res.status}`);

  sleep(1); // 1초 대기
}