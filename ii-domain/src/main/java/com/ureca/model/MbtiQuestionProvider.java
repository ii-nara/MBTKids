package com.ureca.model;

import java.util.List;
import org.springframework.stereotype.Component;

/* 질문 목록 */
@Component
public class MbtiQuestionProvider {

  // 1. 질문 생성 함수
  private static MbtiQuestion createQ(int order, String content, String type) {
    return MbtiQuestion.builder().order(order).content(content).type(type).build();
  }

  // 2. 질문 목록 생성 : 순서, 질문, 성향(부정/긍정)
  public List<MbtiQuestion> getMbtiQuestions() {
    return List.of(
        createQ(
            1,
            "수업을 마치고 놀이터 도착!<br>친구를 만나서 어울려야 나도 쉴 수 있단 생각에 또래 친구를 찾아주려 하는데,<br><br>"
                + "<b>우리 아이는 새로운 친구 사귀기를 좋아한다.</b>",
            "I/E"),
        createQ(
            2,
            "놀이터에서 신나게 놀고 있는 우리 아이.<br>자세히 살펴보니 새로운 놀이를 만들어 놀고 있다!<br><br>"
                + "<b>평소 우리 아이는 기발한 아이디어를 잘 생각해낸다.</b>",
            "S/N"),
        createQ(
            3,
            "놀이터에서 뛰어놀고 집에 온 우리 아이.<br>'오늘은 잘 뛰어놀았으니 금방 자겠지?' 기대했는데<br><br>"
                + "<b>아이는 다음날 일어날 일들에 대한 걱정이 많아 잠에 쉽게 들지 못한다.</b>",
            "P/J"),
        createQ(
            4,
            "수업 시간에 친구들과 토론을 하는 우리 아이.<br>"
                + "우리 아이는 자신의 의견과 다른 친구의 의견에<br><br><b>논리적인 이유 없이 쉽게 공감하지 못하는 편이다.</b>",
            "F/T"),
        createQ(
            5,
            "새로 오픈된 키즈 카페에 놀러왔다!<br>처음 오는 카페에서 재미있게 노는 다른 아이들과 달리,<br><br>"
                + "<b>우리 아이는 긴장해서 익숙해질 때까지 시간이 필요해 보인다.</b>",
            "E/I"),
        createQ(
            6,
            "키즈 카페에 아이가 좋아하는 놀이기구가 많아 보인다!<br>'어떤 것부터 탈거야?' 물어보니,<br><br>"
                + "<b>아이는 타고 싶은 놀이기구를 차례대로 말한다.</b>",
            "P/J"),
        createQ(
            7,
            "'오~ 생각보다 문제를 척척 푸네!'<br>숙제 하는 모습에 감탄하려는 찰나, 모르는 문제 직면!<br><br>"
                + "<b>우리 아이는 문제를 해결하기 위해 개념과 이론을 열심히 탐구하는 편이다.</b>",
            "S/N"),
        createQ(
            8,
            "감동적인 영화를 아이와 함께 보러 왔다!<br>"
                + "결말까지 시청한 우리 아이는 영화에 빠져<br><br><b>등장 인물의 상황에 공감하며 자주 몰입하곤 한다.</b>",
            "T/F"),
        createQ(
            9,
            "선생님이 오시는 날, '곧 학습지 선생님이 오시니 뭘 해야 할까?'<br>"
                + "질문하고 무엇을 하는지 지켜보니<br><br><b>우리 아이의 생각이나 행동에 별다른 계획이나 순서가 없어 보인다.</b>",
            "J/P"),
        createQ(
            10,
            "선생님이 오셨다! 수업 시간에 나누는 대화를 보니,<br>"
                + "우리 아이는 같은 상황이나 사물을 보아도<br><br><b>또래 아이보다 현실적이고 구체적인 묘사를 잘한다.</b>",
            "N/S"),
        createQ(
            11,
            "오랜만에 아이와 함께하는 주말 모임에 다녀왔다!<br>"
                + "친구들과 온종일 열심히 놀고 집에 돌아온<br><br><b>우리 아이는 여전히 에너지가 넘치고 활기차다.</b>",
            "I/E"),
        createQ(
            12,
            "주말 모임에서 생일 파티의 주인공인 아이의 친구가<br>"
                + "많은 선물에 감동을 받고 눈물을 보였다!<br><br><b>옆에서 친구의 모습을 본 우리 아이도 곧 눈물이 고일 것 같다.</b>",
            "T/F"));
  }
}
