package net.i_no_am.glowing_entities.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.glowing_entities.Global;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Version implements Global {

    private static final Map<String, Double> versionCache = new HashMap<>();
    @Nullable private final String api, modId;
    @Nullable private final DownloadType downloadType;
    private static boolean bl = false;
    private final double version;

    /*
        * Creates a new Version instance.
        * @param githubUserName The GitHub username of the mod's repository owner.
        * @param modId The ID of the mod (THIS MUST BE THE NAME OF THE REPO).
        * @param downloadType The type of download source (MODRINTH or CURSEFORGE).
     */
    private Version(@Nullable String githubUserName, @Nullable String modId, @Nullable DownloadType downloadType) throws Exception {
        if (githubUserName == null || modId == null) {
            throw new IllegalArgumentException("GitHub username and mod ID must not be null");
        }

        this.downloadType = downloadType;
        this.modId = modId;
        this.api = "https://api.github.com/repos/%s/%s/releases/latest".formatted(githubUserName, getName());
        this.version = config.shouldCheck ? getVApi() : 0.0;
    }

    public static Version create(@Nullable String githubUserName, @Nullable String modId, @Nullable DownloadType downloadType) {
        try {
            return new Version(githubUserName, modId, downloadType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUpdate(boolean printVersions) {
        if (!bl && mc.currentScreen == null && mc.player != null && !isUpdated() && config.shouldCheck) {
            if (printVersions) System.out.println("Versions: \nCurrent Version: " + getSelf() + "\n" + "Online Version: " + getApi());
            mc.setScreen(new ConfirmScreen(confirmed -> {
                if (confirmed) {
                    Util.getOperatingSystem().open(URI.create(getDownload()));
                    mc.player.closeScreen();
                }
                mc.player.closeScreen();
            }, Text.of(Formatting.RED + "You are using an outdated version of " + getName()), Text.of("Please download the latest version from " + Formatting.GREEN + "Modrinth"), Text.of("Download"), Text.of("Continue playing")));
            bl = true;
        }
    }

    private boolean isUpdated() {
        return getSelf() >= getApi();
    }

    private double getApi() {
        return version;
    }

    private double getSelf() {
        String versionString = FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new RuntimeException(modId + " Isn't loaded")).getMetadata().getVersion().getFriendlyString();
        return parseVersion(versionString);
    }

    private static double parseVersion(String version) {
        String[] parts = version.split("-");

        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                double parsedVersion = Double.parseDouble(versionNumbers[0] + "." + versionNumbers[1]);
                return parsedVersion * 10;
            } else if (part.matches("\\d+\\.\\d+")) return Double.parseDouble(part);
        }

        return 0.0;
    }

    private double getVApi() throws Exception {
        if (versionCache.containsKey(api)) {
            return versionCache.get(api);
        }
        System.out.println("Fetching latest version from API: " + api);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(api)).timeout(Duration.ofSeconds(600)).header("Accept", "application/vnd.github.v3+json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String versionString = jsonResponse.get("tag_name").getAsString();

        double parsedVersion = parseVersion(versionString);
        versionCache.put(api, parsedVersion);
        return parsedVersion;
    }

    @Nullable
    public String getName() {
        return modId.replace("_","-").replaceFirst("^glowing", "Glowing");
    }

    public String getDownload() {
        if (modId == null || downloadType == null) throw new IllegalArgumentException("modId or downloadType is null");
        return switch (downloadType) {
            case MODRINTH -> "https://modrinth.com/mod/%s".formatted(getName());
            case CURSEFORGE -> "https://www.curseforge.com/minecraft/mc-mods/%s".formatted(getName());
        };
    }

    public enum DownloadType {
        MODRINTH, CURSEFORGE
    }
}