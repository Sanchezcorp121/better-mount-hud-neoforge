"""Draws the mod icon with code: a Minecraft-style mount heart over a hunger drumstick,
on a dark rounded tile. Writes icon.png (512px, for store pages) and copies a 128px version into every version's
resources as bettermounthud.png (the logoFile in neoforge.mods.toml)."""
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parent.parent
PX = 16  # pixel-art grid

HEART = [
    "....XX...XX.....",
    "...XXXX.XXXX....",
    "..XXXXXXXXXXX...",
    "..XXXXXXXXXXX...",
    "..XXXXXXXXXXX...",
    "...XXXXXXXXX....",
    "....XXXXXXX.....",
    ".....XXXXX......",
    "......XXX.......",
    ".......X........",
]
DRUMSTICK = [
    "......XXX.",
    ".....XXXXX",
    ".....XXXXX",
    "....XXXXX.",
    "...XXXX...",
    "..XXX.....",
    ".XX.......",
    "XX........",
]

def draw(size: int) -> Image.Image:
    s = size / PX
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rounded_rectangle((0, 0, size - 1, size - 1), radius=int(size * 0.18), fill=(38, 32, 48, 255))
    d.rounded_rectangle((int(s * 0.5), int(s * 0.5), size - int(s * 0.5) - 1, size - int(s * 0.5) - 1),
                        radius=int(size * 0.15), outline=(70, 60, 86, 255), width=max(1, int(s * 0.35)))

    def blit(rows, ox, oy, fill, shade):
        for y, row in enumerate(rows):
            for x, ch in enumerate(row):
                if ch == "X":
                    x0, y0 = (ox + x) * s, (oy + y) * s
                    d.rectangle((x0, y0, x0 + s - 1, y0 + s - 1), fill=fill)
        # bottom-right shading pass for a little depth
        for y, row in enumerate(rows):
            for x, ch in enumerate(row):
                if ch == "X" and (y + 1 >= len(rows) or x >= len(rows[y + 1]) or rows[y + 1][x] != "X"):
                    x0, y0 = (ox + x) * s, (oy + y) * s
                    d.rectangle((x0, y0 + s * 0.6, x0 + s - 1, y0 + s - 1), fill=shade)

    # mount heart: the gold vehicle heart colours from the vanilla HUD
    blit(HEART, 0, 1, (232, 168, 44, 255), (176, 112, 20, 255))
    # drumstick, bottom right, meat then bone
    blit(DRUMSTICK, 6, 7, (178, 96, 48, 255), (120, 60, 28, 255))
    for (x, y) in [(6, 13), (7, 13), (6, 14), (7, 14), (6, 12), (7, 12)]:
        x0, y0 = x * s, y * s
        d.rectangle((x0, y0, x0 + s - 1, y0 + s - 1), fill=(236, 226, 206, 255))
    return img

if __name__ == "__main__":
    draw(512).save(ROOT / "icon.png")
    small = draw(128)
    for res in (ROOT / "versions").glob("*/src/main/resources"):
        small.save(res / "bettermounthud.png")
    print("icon written")
