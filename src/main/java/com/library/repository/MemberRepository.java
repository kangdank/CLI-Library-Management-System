package com.library.repository;

import com.library.model.Member;

import java.util.*;
import java.util.stream.Collectors;

public class MemberRepository {
    private final Map<String, Member> members;

    public MemberRepository() {
        this.members = new HashMap<>();
    }

    public void save(Member member) {
        if (member != null && member.getMemberId() != null) {
            members.put(member.getMemberId(), member);
        }
    }

    public Optional<Member> findById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    public Optional<Member> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        return members.values().stream()
                .filter(member -> member.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Member> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchTerm = name.toLowerCase();
        return members.values().stream()
                .filter(member -> member.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Member> findAllActive() {
        return members.values().stream()
                .filter(Member::isActive)
                .collect(Collectors.toList());
    }

    public List<Member> findAll() {
        return new ArrayList<>(members.values());
    }

    public boolean deleteById(String memberId) {
        return members.remove(memberId) != null;
    }

    public void update(Member member) {
        if (member != null && member.getMemberId() != null && members.containsKey(member.getMemberId())) {
            members.put(member.getMemberId(), member);
        }
    }

    public boolean existsById(String memberId) {
        return members.containsKey(memberId);
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public long count() {
        return members.size();
    }

    public long countActive() {
        return members.values().stream()
                .filter(Member::isActive)
                .count();
    }

    public void clear() {
        members.clear();
    }

    public List<Member> findMembersWithBorrowedBooks() {
        return members.values().stream()
                .filter(member -> member.getBorrowedBookCount() > 0)
                .collect(Collectors.toList());
    }
}