package com.library.service;

import com.library.exception.InvalidOperationException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import com.library.util.InputValidator;

import java.util.List;
import java.util.UUID;

public class MemberService {
    private final MemberRepository memberRepository;
    private static final int MAX_BORROW_LIMIT = 5;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member registerMember(String name, String email, String phone)
            throws InvalidOperationException {

        if (!InputValidator.isNotEmpty(name)) {
            throw new InvalidOperationException("Không được để trống tên");
        }
        if (!InputValidator.isValidEmail(email)) {
            throw new InvalidOperationException("Sai định dạng Email: " + email);
        }
        if (!InputValidator.isValidPhone(phone)) {
            throw new InvalidOperationException("Sai định dạng số điện thoại: " + phone);
        }
        if (memberRepository.existsByEmail(email)) {
            throw new InvalidOperationException("Thành viên với Emil " + email + " đã tồn tại");
        }

        String memberId = generateMemberId();

        Member member = new Member(memberId, name, email, phone);
        memberRepository.save(member);

        return member;
    }

    public Member findMemberById(String memberId) throws MemberNotFoundException {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.forId(memberId));
    }

    public Member findMemberByEmail(String email) throws MemberNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.forEmail(email));
    }

    public List<Member> searchMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getActiveMembers() {
        return memberRepository.findAllActive();
    }

    public void deactivateMember(String memberId) throws MemberNotFoundException, InvalidOperationException {
        Member member = findMemberById(memberId);

        if (member.getBorrowedBookCount() > 0) {
            throw new InvalidOperationException(
                    "Không thể hủy thành viên đang mượn sách. ID Thành viên: " + memberId);
        }
        if (!member.isActive()) {
            throw new InvalidOperationException(
                    "Thành viên này đã ở trạng thái không hoạt động: " + memberId);
        }


        member.setActive(false);
        memberRepository.update(member);
    }

    public void activateMember(String memberId) throws MemberNotFoundException, InvalidOperationException {
        Member member = findMemberById(memberId);
        if (member.isActive()) {
            throw new InvalidOperationException(
                    "Thành viên này đã ở trạng thái hoạt động: " + memberId);
        }
        member.setActive(true);
        memberRepository.update(member);
    }

    public void updateMember(String memberId, String name, String email, String phone)
            throws MemberNotFoundException, InvalidOperationException {

        Member member = findMemberById(memberId);

        if (InputValidator.isNotEmpty(name)) {
            member.setName(name);
        }
        if (InputValidator.isNotEmpty(email)) {
            if (!InputValidator.isValidEmail(email)) {
                throw new InvalidOperationException("Sai định dạng Email:: " + email);
            }
            if (!email.equals(member.getEmail()) && memberRepository.existsByEmail(email)) {
                throw new InvalidOperationException("Email đã được sử dụng: " + email);
            }
            member.setEmail(email);
        }
        if (InputValidator.isNotEmpty(phone)) {
            if (!InputValidator.isValidPhone(phone)) {
                throw new InvalidOperationException("Sai định dạng số điện thoại: " + phone);
            }
            member.setPhone(phone);
        }

        memberRepository.update(member);
    }

    public boolean canBorrowBooks(String memberId) throws MemberNotFoundException {
        Member member = findMemberById(memberId);
        return member.isActive() && member.getBorrowedBookCount() < MAX_BORROW_LIMIT;
    }

    public int getBorrowedBookCount(String memberId) throws MemberNotFoundException {
        Member member = findMemberById(memberId);
        return member.getBorrowedBookCount();
    }

    public long getTotalMemberCount() {
        return memberRepository.count();
    }

    public long getActiveMemberCount() {
        return memberRepository.countActive();
    }

    public List<Member> getMembersWithBorrowedBooks() {
        return memberRepository.findMembersWithBorrowedBooks();
    }

    private String generateMemberId() {
        String memberId;
        do {
            memberId = "MEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (memberRepository.existsById(memberId));
        return memberId;
    }

    public int getMaxBorrowLimit() {
        return MAX_BORROW_LIMIT;
    }
}