package com.urlshortener.url_shortener;

import com.urlshortener.url_shortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.urlshortener.url_shortener.exception.AliasAlreadyExistsException;
import com.urlshortener.url_shortener.exception.InvalidAliasException;
import com.urlshortener.url_shortener.exception.InvalidUrlException;
import com.urlshortener.url_shortener.exception.ShortUrlNotFoundException;
import com.urlshortener.url_shortener.model.UrlMapping;
import com.urlshortener.url_shortener.repository.InMemoryUrlRepository;
import com.urlshortener.url_shortener.repository.UrlRepository;
import com.urlshortener.url_shortener.strategy.IdGenerationStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UrlShortenerApplicationTests {

	@Test
	void shortenUrlWithoutAliasReturnsGeneratedShortUrl() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		String shortUrl = service.shortenUrl("https://example.com", null);
		assertEquals("abc123", shortUrl);
	}

	@Test
	void shortenUrlWithAliasReturnsAlias() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		String shortUrl = service.shortenUrl("https://example.com", "my-link");
		assertEquals("my-link", shortUrl);
	}

	@Test
	void duplicateAliasThrowsException() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		service.shortenUrl("https://example.com", "my-link");

		assertThrows(
				AliasAlreadyExistsException.class,
				() -> service.shortenUrl("https://google.com", "my-link")
		);
	}

	@Test
	void sameLongUrlReturnsSameShortUrl() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		String first = service.shortenUrl("https://example.com", null);
		String second = service.shortenUrl("https://example.com", null);

		assertEquals(first, second);
	}

	@Test
	void getOriginalUrlReturnsLongUrl() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		String shortUrl = service.shortenUrl("https://example.com", null);

		assertEquals("https://example.com", service.getOriginalUrl(shortUrl));
	}

	@Test
	void getOriginalUrlIncrementsClickCount() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		String shortUrl = service.shortenUrl("https://example.com", null);

		service.getOriginalUrl(shortUrl);
		service.getOriginalUrl(shortUrl);

		UrlMapping details = service.getUrlDetails(shortUrl);

		assertEquals(2, details.getClickCount());
	}

	@Test
	void unknownShortUrlThrowsException() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		assertThrows(
				ShortUrlNotFoundException.class,
				() -> service.getOriginalUrl("missing")
		);
	}
	@Test
	void blankLongUrlThrowsException() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		assertThrows(
				InvalidUrlException.class,
				() -> service.shortenUrl("", null)
		);
	}

	@Test
	void invalidAliasThrowsException() {
		UrlRepository repository = new InMemoryUrlRepository();
		IdGenerationStrategy strategy = longUrl -> "abc123";
		UrlShortenerService service = new UrlShortenerService(repository, strategy);

		assertThrows(
				InvalidAliasException.class,
				() -> service.shortenUrl("https://example.com", "my link")
		);
	}
}

