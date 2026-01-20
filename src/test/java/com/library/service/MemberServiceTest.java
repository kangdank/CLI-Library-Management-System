package com.library.service;

import com.library.exception.InvalidOperationException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @Test
    void testRegisterMember_WithValidData_Success() throws InvalidOperationException {
        Member member = memberService.registerMember(
                "John Doe",
                "john.doe@email.com",
                "1234567890"
        );

        assertNotNull(member);
        assertNotNull(member.getMemberId());
        assertEquals("John Doe", member.getName());
        assertEquals("john.doe@email.com", member.getEmail());
        assertTrue(member.isActive());
    }

    @Test
    void testRegisterMember_WithInvalidEmail_ThrowsException() {
        assertThrows(InvalidOperationException.class, () -> {
            memberService.registerMember("John Doe", "invalid-email", "1234567890");
        });
    }

    @Test
    void testRegisterMember_WithEmptyName_ThrowsException() {
        assertThrows(InvalidOperationException.class, () -> {
            memberService.registerMember("", "john@email.com", "1234567890");
        });
    }

    @Test
    void testRegisterMember_WithDuplicateEmail_ThrowsException() throws InvalidOperationException {
        memberService.registerMember("John Doe", "john@email.com", "1234567890");

        assertThrows(InvalidOperationException.class, () -> {
            memberService.registerMember("Jane Doe", "john@email.com", "0987654321");
        });
    }

    @Test
    void testFindMemberById_ExistingMember_ReturnsMember() throws Exception {
        Member registered = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        Member found = memberService.findMemberById(registered.getMemberId());

        assertNotNull(found);
        assertEquals(registered.getMemberId(), found.getMemberId());
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testFindMemberById_NonExistingMember_ThrowsException() {
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.findMemberById("NON-EXISTING-ID");
        });
    }

    @Test
    void testFindMemberByEmail_ExistingMember_ReturnsMember() throws Exception {
        memberService.registerMember("John Doe", "john@email.com", "1234567890");

        Member found = memberService.findMemberByEmail("john@email.com");

        assertNotNull(found);
        assertEquals("john@email.com", found.getEmail());
    }

    @Test
    void testDeactivateMember_WithoutBorrowedBooks_Success() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        memberService.deactivateMember(member.getMemberId());

        Member updated = memberService.findMemberById(member.getMemberId());
        assertFalse(updated.isActive());
    }

    @Test
    void testDeactivateMember_WithBorrowedBooks_ThrowsException() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        // Simulate borrowing a book
        member.borrowBook("978-0132350884");

        assertThrows(InvalidOperationException.class, () -> {
            memberService.deactivateMember(member.getMemberId());
        });
    }

    @Test
    void testActivateMember_Success() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        memberService.deactivateMember(member.getMemberId());
        memberService.activateMember(member.getMemberId());

        Member updated = memberService.findMemberById(member.getMemberId());
        assertTrue(updated.isActive());
    }

    @Test
    void testCanBorrowBooks_ActiveMemberUnderLimit_ReturnsTrue() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        assertTrue(memberService.canBorrowBooks(member.getMemberId()));
    }

    @Test
    void testCanBorrowBooks_InactiveMember_ReturnsFalse() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        memberService.deactivateMember(member.getMemberId());

        assertFalse(memberService.canBorrowBooks(member.getMemberId()));
    }

    @Test
    void testUpdateMember_WithValidEmail_Success() throws Exception {
        Member member = memberService.registerMember(
                "John Doe",
                "john@email.com",
                "1234567890"
        );

        memberService.updateMember(
                member.getMemberId(),
                "John Updated",
                "john.updated@email.com",
                "9876543210"
        );

        Member updated = memberService.findMemberById(member.getMemberId());
        assertEquals("John Updated", updated.getName());
        assertEquals("john.updated@email.com", updated.getEmail());
        assertEquals("9876543210", updated.getPhone());
    }

    @Test
    void testGetTotalMemberCount_ReturnsCorrectCount() throws InvalidOperationException {
        memberService.registerMember("John Doe", "john@email.com", "1234567890");
        memberService.registerMember("Jane Doe", "jane@email.com", "0987654321");

        assertEquals(2, memberService.getTotalMemberCount());
    }

    @Test
    void testGetActiveMemberCount_ReturnsCorrectCount() throws Exception {
        Member member1 = memberService.registerMember("John Doe", "john@email.com", "1234567890");
        memberService.registerMember("Jane Doe", "jane@email.com", "0987654321");

        memberService.deactivateMember(member1.getMemberId());

        assertEquals(1, memberService.getActiveMemberCount());
    }
}