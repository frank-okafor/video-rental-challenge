package com.video.rental.challenge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.video.rental.challenge.entity.User;
import com.video.rental.challenge.entity.Video;
import com.video.rental.challenge.helper.utils.ServiceTestHelper;

@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest {
    private @Autowired UserRepository userRepo;
    private @Autowired VideoRepository videoRepo;

    // testing the repository layer as a spring jpa test case
    @BeforeEach
    void setUp() throws Exception {
	userRepo.deleteAll();
	videoRepo.deleteAll();
    }

    @Test
    void testFindUserByUsername() {
	// given
	User user = ServiceTestHelper.userToCreate();
	// when
	userRepo.save(user);
	// then
	assertThat(userRepo.findByUsername(user.getUsername()).isPresent()).isTrue();
    }

    @Test
    void testFindVideoByTitle() {
	// given
	Video video = ServiceTestHelper.videoToCreate();
	// when
	videoRepo.save(video);
	// then
	assertThat(videoRepo.findByTitle(video.getTitle()).isPresent()).isTrue();
    }

    @Test
    void checkIfAllVideosCanBeSavesAndGotten() {
	// when
	videoRepo.saveAll(ServiceTestHelper.videoList());
	// then
	assertThat(videoRepo.findAll().isEmpty()).isFalse();
    }

}
