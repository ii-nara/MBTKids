import http from 'k6/http';
import {sleep} from 'k6';

export const options = { // 성능 테스트 옵션값
  scenarios: {
    contacts: {
      executor: 'constant-arrival-rate',
      duration: '1m',
      rate: '1700',
      timeUnit: '1s',
      preAllocatedVUs: '50',
      maxVUs: '200',
    },
  },
};

export default function () {
  http.get(
      'http://mbtkids-lb-fargate-1263411959.ap-northeast-2.elb.amazonaws.com:8080/mbtkids');
  sleep(0.5);
}