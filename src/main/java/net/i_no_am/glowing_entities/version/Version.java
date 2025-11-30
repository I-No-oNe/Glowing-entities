package net.i_no_am.glowing_entities.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.glowing_entities.Global;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Version implements Global {

    private static final Map<String, Double> versionCache = new HashMap<>();
    private static boolean hadPrompted = false, sendOnce = false;
    private final String name, downloadUrl, modId, apiUrl;
    private final boolean printVersions;
    private final Supplier<Boolean> condition;

    private Version(Builder builder) {
        this.name = builder.name;
        this.modId = builder.modId;
        this.apiUrl = "https://api.github.com/repos/%s/%s/releases/latest".formatted(builder.gitUsername, builder.modId);
        this.downloadUrl = builder.downloadSource;
        this.printVersions = builder.printVersions;
        this.condition = builder.condition;
    }

    public static class Builder {

        private Builder() {
        }

        private String name;
        private String modId;
        private String gitUsername;
        private String downloadSource;
        private boolean printVersions = false;
        private Supplier<Boolean> condition = () -> true;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder modId(String modId) {
            this.modId = modId;
            return this;
        }

        public Builder gitUsername(String gitUsername) {
            this.gitUsername = gitUsername;
            return this;
        }

        public Builder downloadSource(String url) {
            this.downloadSource = url;
            return this;
        }

        public Builder printVersions(boolean value) {
            this.printVersions = value;
            return this;
        }

        public Builder condition(Supplier<Boolean> condition) {
            this.condition = condition;
            return this;
        }

        public Version build() {
            return new Version(this);
        }
    }

    public void notifyUpdates() {
        if (!shouldCheck() || hadPrompted) return;
        try {
            double localVersion = getLocalVersion();
            double remoteVersion = getRemoteVersion();

            if (printVersions && !sendOnce) {
                System.out.println("[Glowing-Entities] Version Check:\nLocal: " + localVersion + "\nRemote: " + remoteVersion);
                sendOnce = true;
            }
            if (localVersion < remoteVersion && mc.player != null && mc.currentScreen == null) {
                hadPrompted = true;

                mc.setScreen(new ConfirmScreen(confirmed -> {
                    if (confirmed) {
                        Util.getOperatingSystem().open(URI.create(downloadUrl));
                    }
                    mc.player.closeScreen();
                }, Text.of(Formatting.RED + name + " is outdated!"),
                        Text.of("Please download the latest version from " + Formatting.GREEN + "Modrinth"),
                        Text.of("Download"), Text.of("Continue playing")));
            }

        } catch (Exception e) {
            System.out.println("[Glowing-Entities] Version check failed: " + e.getMessage());
            hadPrompted = true;
        }
    }

    private boolean shouldCheck() {
        return condition.get();
    }

    private double getRemoteVersion() throws Exception {
        if (versionCache.containsKey(apiUrl)) {
            return versionCache.get(apiUrl);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP error " + response.statusCode());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        String tag = json.get("tag_name").getAsString();

        double parsedVersion = parseVersion(tag);
        versionCache.put(apiUrl, parsedVersion);
        return parsedVersion;
    }

    private double getLocalVersion() {
        String versionString = FabricLoader.getInstance().getModContainer("glowing_entities")
                .orElseThrow(() -> new RuntimeException("glowing_entities" + " isn't loaded"))
                .getMetadata().getVersion().getFriendlyString();
        return parseVersion(versionString);
    }

    private static int parseVersion(String version) {
        String[] parts = version.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                int major = Integer.parseInt(versionNumbers[0]);
                int minor = Integer.parseInt(versionNumbers[1]);
                int patch = Integer.parseInt(versionNumbers[2]);
                return major * 10000 + minor * 100 + patch;
            } else if (part.matches("\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                int major = Integer.parseInt(versionNumbers[0]);
                int minor = Integer.parseInt(versionNumbers[1]);
                return major * 10000 + minor * 100;
            }
        }
        return 0;
    }

    public static Builder builder() {
        return new Builder();
    }
}