package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Member
import com.midas.newscollector.dto.MemberDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*

@DisplayName("리포지토리 테스트 - Member")
@DataJpaTest
@Import(JpaConfig::class)
@ActiveProfiles("testdb")
class MemberRepositoryTest(@Autowired private val memberRepository: MemberRepository) {

    @DisplayName("Member를 저장하면 Member가 반환된다.")
    @Test
    fun givenMember_whenSaveMember_thenReturnsMember() {
        //given
        val member = Member(memberId = "testuser", email = "test@test.com", password = "12345678")
        //when
        val savedMember: Member = memberRepository.save(member)
        //then
        assertThat(savedMember).isNotNull()
        assertThat(savedMember.memberId).isEqualTo(member.memberId)
        assertThat(savedMember.getCreatedAt()).isNotNull()
        assertThat(savedMember.getUpdatedAt()).isNotNull()
    }

    @DisplayName("memberId로 조회를 하면 Optional Member가 반환된다.")
    @Test
    fun givenMemberId_whenFindMember_thenReturnsOptionalMember() {
        //given
        val memberId: String = createNewMember().memberId
        //when
        val member = memberRepository.getMemberByMemberId(memberId)
        //then
        assertThat(member).isNotNull
    }

    @DisplayName("member를 수정하면 Member가 변환된다.")
    @Test
    fun givenSavedMember_whenUpdateMember_thenChangesMember() {
        //given
        val savedMember = createNewMember()
        val parameter = MemberDto(memberId = "test", email = "0000@test.com", password = "46145")
        //when
        savedMember.update(parameter)
        //then
        val testMember = memberRepository.getMemberByMemberId(savedMember.memberId)
        assertThat(testMember).isNotNull
        testMember?.let {
            assertThat(it.memberId).isNotEqualTo("test")
            assertThat(it.getEmail()).isEqualTo("0000@test.com")
            assertThat(it.getPassword()).isNotEqualTo("46145")
            assertThat(it.getCreatedAt()).isNotEqualTo(it.getUpdatedAt())
        }
    }

    /**
     * 테스트용 멤버 생성
     * @return
     */
    private fun createNewMember(): Member {
        val member = Member(memberId = "testuser", email = "test@test.com", password = "12345678")
        return memberRepository.save(member)
    }

}