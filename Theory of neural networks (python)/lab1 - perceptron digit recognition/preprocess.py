from random import randint
from PIL import Image

# подготовка дата сета
for i in range(10):
    name = f"digits/{i}.bmp"
    for j in range(10):
        image = Image.open(name)
        image.putpixel((randint(0, image.width - 1), randint(0, image.height - 1)), 100)
        image.save(f"digits/{i}_{j}.bmp")
