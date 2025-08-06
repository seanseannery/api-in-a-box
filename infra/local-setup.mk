.PHONY: setup check-dependencies

# Detect shell type
SHELL_TYPE := $(shell echo $$SHELL)
ifeq ($(findstring zsh,$(SHELL_TYPE)),zsh)
	SHELL_CONFIG := ~/.zshrc
	SHELL_NAME := zsh
else ifeq ($(findstring bash,$(SHELL_TYPE)),bash)
	SHELL_CONFIG := ~/.bashrc
	SHELL_NAME := bash
else
	SHELL_CONFIG := ~/.bashrc
	SHELL_NAME := bash
endif

# Setup development environment
setup: check-dependencies
	@echo "Development environment setup complete!"

# Check and install dependencies
check-dependencies:
	@echo "Checking and installing dependencies for $(SHELL_NAME) shell"
	@if ! command -v brew &> /dev/null; then \
		echo "Installing Homebrew..."; \
		/bin/bash -c "$$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"; \
	fi
	@if ! command -v jenv &> /dev/null; then \
		echo "Installing jenv..."; \
		brew install jenv; \
		echo 'export PATH="$HOME/.jenv/bin:$$PATH"' >> $(SHELL_CONFIG); \
		if [ "$(SHELL_NAME)" = "zsh" ]; then \
			echo 'eval "$$(jenv init -)"' >> $(SHELL_CONFIG); \
		else \
			echo 'eval "$$(jenv init - bash)"' >> $(SHELL_CONFIG); \
		fi; \
	fi
	@if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "21"; then \
		echo "Installing Java 24..."; \
		brew install openjdk@24; \
		echo "Linking Java 24..."; \
		echo 'export PATH="/opt/homebrew/opt/openjdk@24/bin:$PATH"' >> $(SHELL_CONFIG); \
		sudo ln -sfn /opt/homebrew/opt/openjdk@24/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-24.jdk; \
		mkdir -p ~/.jenv/versions; \
		jenv add /Library/Java/JavaVirtualMachines/openjdk-24.jdk/Contents/Home; \
		jenv global 24; \
		jenv enable-plugin export; \
		echo "Java 24 installation complete. Please restart your terminal or run: source $(SHELL_CONFIG)"; \
	fi
	@if ! command -v mvn &> /dev/null; then \
		echo "Installing Maven..."; \
		brew install maven; \
	fi
	@if ! command -v docker &> /dev/null; then \
		echo "Installing Docker..."; \
		brew install --cask docker; \
		echo "Please start Docker Desktop and run this command again"; \
		exit 1; \
	fi
	@if ! command -v docker-compose &> /dev/null; then \
		echo "Installing Docker Compose..."; \
		brew install docker-compose; \
	fi
	@echo "All dependencies are installed and configured." 