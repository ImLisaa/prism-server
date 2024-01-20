package net.bytebridges.prism.server.instance.type;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public enum InstanceType {

    FLAT(new FlatGenerator()),
    VOID(new VoidGenerator());

    private final Generator generator;

    InstanceType(Generator generator) {
        this.generator = generator;
    }

    public Generator getGenerator() {
        return generator;
    }


    private static class FlatGenerator implements Generator {

        @Override
        public void generate(@NotNull GenerationUnit unit) {
            unit.modifier().fillHeight(-1, 0, Block.GRASS_BLOCK);
            unit.modifier().fillHeight(-2, -1, Block.DIRT);
            unit.modifier().fillHeight(-3, -2, Block.DIRT);
            unit.modifier().fillHeight(-4, -3, Block.BEDROCK);
        }
    }

    private static class VoidGenerator implements Generator {

        @Override
        public void generate(@NotNull GenerationUnit unit) {
            unit.modifier().fill(new Pos(0, 0, 0), new Pos(0, 0, 0), Block.GOLD_BLOCK);
        }
    }
}
