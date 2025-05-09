package sk.mkrajcovic.bgs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class GlobalException extends RuntimeException {

	private static final long serialVersionUID = -1413853990238635218L;

	private final HttpStatus httpStatus;
	private final String code;
	private final transient Object[] args;
	private final List<String> hints;

	protected GlobalException(@NonNull HttpStatus httpStatus, @NonNull String code, Throwable cause, List<String> hints, Object... args) {
		super(httpStatus + ", " + code + ": " + Arrays.toString(args), cause);
		this.httpStatus = httpStatus;
		this.code = code;
		this.hints = hints != null ? new ArrayList<>(hints) : new ArrayList<>();
		this.args = args != null? Arrays.copyOf(args, args.length) : null;
	}

	public List<String> getHints() {
		return new ArrayList<>(hints);
	}

	public final void addHint(String hint) {
		hints.add(hint);
	}

	public Object[] getArgs() {
		return args != null? Arrays.copyOf(args, args.length) : new Object[0];
	}

	/**
	 * Designed for internal use only.
	 */
	public void clearHints() {
		this.hints.clear();
	}

}
