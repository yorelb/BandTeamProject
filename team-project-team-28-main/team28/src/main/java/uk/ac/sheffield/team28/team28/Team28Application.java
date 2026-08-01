package uk.ac.sheffield.team28.team28;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.util.Optional;

@SpringBootApplication
public class Team28Application {

	public static void main(String[] args) {
		//TODO: HardCode director
		SpringApplication.run(Team28Application.class, args);
	}

	@Bean
	public CommandLineRunner createDirector(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			String dirFirstName = "Director";
			String dirLastName = "Director";
			String dirEmail = "Director@test.com";
			String dirNumber = "0123456789";
			String dirPassword = "Director1";
			MemberType memberType = MemberType.DIRECTOR;
			Optional<Member> maybeDirector = memberRepository.findByEmail(dirEmail);
			if (maybeDirector.isEmpty()) {
				Member director = new Member();
				director.setEmail(dirEmail);
				director.setFirstName(dirFirstName);
				director.setLastName(dirLastName);
				director.setPassword(passwordEncoder.encode(dirPassword));
				director.setMemberType(MemberType.DIRECTOR);
				director.setPhone(dirNumber);
				memberRepository.save(director);
			}
		};

	}

}
