package net.phantara.prism.server.schematic;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jglrxavpok.hephaistos.nbt.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 15:54
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class Schematic {

    private final Map<Vec, Block> blocks = new LinkedHashMap<>();

    public void addBlock(Vec vec, Block block) {
        this.blocks.put(vec, block);
    }

    public void save(File file) {
        var level = NBT.Compound(root -> {
            var compounds = new LinkedList<NBTCompound>();
            for (Vec vec : this.blocks.keySet()) {
                var block = this.blocks.get(vec);
                compounds.add(NBT.Compound(it -> {
                    it.setInt("x", vec.blockX()).setInt("y", vec.blockY()).setInt("z", vec.blockZ()).setString("block", block.namespace().namespace());
                    it.set("hasData", NBT.Boolean(block.nbt() != null));
                    if (block.nbt() != null) {
                        it.set("data", block.nbt());
                    }
                }));
            }
            root.set("blocks", NBT.List(NBTType.TAG_Compound, compounds));
        });
        try (NBTWriter writer = new NBTWriter(file, CompressedProcesser.GZIP)) {
            writer.writeNamed("", level);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void paste(Pos pos, Instance instance) {
        for (var vec : this.blocks.keySet()) {
            instance.setBlock(pos.add(vec), this.blocks.get(vec));
        }
    }
}
