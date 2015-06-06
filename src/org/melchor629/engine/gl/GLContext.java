package org.melchor629.engine.gl;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

//import static org.lwjgl.opengl.GL14.*;

/**
 * Enclosing interface for every renderer, and a bunch of variables in enums
 * for some functions that simplify coding. Is incomplete, but is actualy
 * functional.
 * @author melchor9000
 */
public interface GLContext {

    /**
     * Enum for select some capability to enable/disable (maybe incomplete)
     * @author melchor9000
     */
    enum GLEnable {
        BLEND (0xBE2),
        CLIP_DISTANCE_0 (0x3000),
        CLIP_DISTANCE_1 (0x3001),
        CLIP_DISTANCE_2 (0x3002),
        CLIP_DISTANCE_3 (0x3003),
        CLIP_DISTANCE_4 (0x3004),
        CLIP_DISTANCE_5 (0x3005),
        CLIP_DISTANCE_6 (0x3006),
        CLIP_DISTANCE_7 (0x3007),
        COLOR_LOGIC_OP (0xBF2),
        CULL_FACE (0xB44),
        DEPTH_TEST (0xB71),
        /** REQUIRES GL 3.2 **/
        DEPTH_CLAMP (0x864F),
        DITHER (0xBD0),
        FRAMEBUFFER_SRGB (0x8DB9),
        LINE_SMOOTH (0xB20),
        MULTISAMPLE (0x809D),
        POLYGON_OFFSET_FILL (0x8037),
        POLYGON_OFFSET_LINE (0x2A02),
        POLYGON_OFFSET_POINT (0x2A01),
        POLYGON_SMOOTH (0xB41),
        /** REQUIRES GL 3.1 **/
        PRIMITIVE_RESTART (0x8F9D),
        SAMPLE_ALPHA_TO_COVERAGE (0x809E),
        SAMPLE_ALPHA_TO_ONE (0x809F),
        SAMPLE_COVERAGE_INVERT (0x80AB),
        SAMPLE_COVERAGE (0x80A0),
        SCRISSOR_TEST (0xC11),
        STENCIL_TEST (0xB90),
        /** REQUIRE GL 3.2 **/
        TEXTURE_CUBE_MAP_SEAMLESS (0x884F),
        /** REQUIRE GL 3.2 **/
        PROGRAM_POINT_SIZE (0x8642);

        final int e;
        GLEnable(int a) { this.e = a; }
        
    }

    /**
     * Enum with Buffer Targets for a glBindBuffer (for example)
     * @author melchor9000
     */
    enum BufferTarget {
        ARRAY_BUFFER (0x8892),
        /** GL 3.1 **/
        COPY_READ_BUFFER (0x8F36),
        /** GL 3.1 **/
        COPY_WRITE_BUFFER (0x8F37),
        ELEMENT_ARRAY_BUFFER (0x8893),
        PIXEL_PACK_BUFFER (0x88EB),
        PIXEL_UNPACK_BUFFER (0x88EC),
        /** GL 3.1 **/
        TEXTURE_BUFFER (0x8C2A),
        TRANSFORM_FEEDBACK_BUFFER (0x8C8E),
        /** GL 3.1 **/
        UNIFORM_BUFFER (0x8A11);

        final int e;
        BufferTarget(int t) { e = t; }
    }

    /**
     * Enum for Buffer Object usage
     * @author melchor9000
     */
    enum BufferUsage {
        STREAM_DRAW (0x88E0),
        STREAM_READ (0x88E1),
        STREAM_COPY (0x88E2),
        STATIC_DRAW (0x88E4),
        STATIC_READ (0x88E5),
        STATIC_COPY (0x88E6),
        DYNAMIC_DRAW (0x88E8),
        DYNAMIC_READ (0x88E9),
        DYNAMIC_COPY (0x88EA);

        final int e;
        BufferUsage(int t) { e = t; }
    }

    /**
     * Enum with types of shaders
     * @author melchor9000
     */
    enum ShaderType {
        VERTEX, FRAGMENT, GEOMETRY
    }

    /**
     * Enum of variable types
     * @author melchor9000
     */
    enum type {
        BYTE (0x1400),
        UNSIGNED_BYTE (0x1401),
        SHORT (0x1402),
        UNSIGNED_SHORT (0x1403),
        INT (0x1404),
        UNSIGNED_INT (0x1405),
        HALF_FLOAT (0x140B),
        FLOAT (0x1406),
        DOUBLE (0x140A),
        UNSIGNED_BYTE_3_3_2 (0x8032),
        UNSIGNED_BYTE_2_3_3_REV (0x8362),
        UNSIGNED_SHORT_5_6_5 (0x8363),
        UNSIGNED_SHORT_5_6_5_REV (0x8364),
        UNSIGNED_SHORT_4_4_4_4 (0x8033),
        UNSIGNED_SHORT_4_4_4_4_REV (0x8365),
        UNSIGNED_SHORT_5_5_5_1 (0x8034),
        UNSIGNED_SHORT_1_5_5_5_REV (0x8366),
        UNSIGNED_INT_8_8_8_8 (0x8035),
        UNSIGNED_INT_8_8_8_8_REV (0x8367),
        UNSIGNED_INT_10_10_10_2 (0x8036),
        UNSIGNED_INT_2_10_10_10_REV (0x8368);
        
        final int e;
        type(int t) { e = t; }
    }

    /**
     * Enum with Texture Targets
     * @author melchor9000
     * @see <a href="http://www.opengl.org/sdk/docs/man/xhtml/glTexImage1D.xml">glTexImage1D</a>
     * @see <a href="http://www.opengl.org/sdk/docs/man/xhtml/glTexImage2D.xml">glTexImage2D</a>
     * @see <a href="http://www.opengl.org/sdk/docs/man/xhtml/glTexImage3D.xml">glTexImage3D</a>
     */
    enum TextureTarget {
        //1D
        TEXTURE_1D (0xDE0),
        PROXY_TEXTURE_1D (0x8063),

        //2D
        TEXTURE_2D (0xDE1),
        PROXY_TEXTURE_2D (0x8064),
        TEXTURE_1D_ARRAY (0x8C18),
        PROXY_TEXTURE_1D_ARRAY (0x8C19),
        TEXTURE_RECTANGLE (0x84F5),
        PROXY_TEXTURE_RECTANGLE (0x84F7),
        TEXTURE_CUBE_MAP_POSITIVE_X (0x8515),
        TEXTURE_CUBE_MAP_NEGATIVE_X (0x8516),
        TEXTURE_CUBE_MAP_POSITIVE_Y (0x8517),
        TEXTURE_CUBE_MAP_NEGATIVE_Y (0x8518),
        TEXTURE_CUBE_MAP_POSITIVE_Z (0x8519),
        TEXTURE_CUBE_MAP_NEGATIVE_Z (0x851A),
        PROXY_TETURE_CUBE_MAP (0x851B),

        //3D
        TEXTURE_3D (0x806F),
        PROXY_TEXTURE_3D (0x8070),
        TEXTURE_2D_ARRAY (0x8C1A),
        PROXY_TEXTURE_2D_ARRAY (0x8C1B);

        final int e;
        TextureTarget(int t) { e = t; }
    }

    /**
     * Enum with internal formats for Textures
     * @author melchor9000
     **/
    enum TextureFormat {
        GREEN (0x1904),
        BLUE (0x1905),
        ALPHA (0x1906),
        //RED
        RED (0x1903),
        R8 (0x8229),
        R8_SNORM (0x8F94),
        R16 (0x822A),
        R16_SNROM (0x8F98),
        R16F (0x822D),
        R32F (0x822E),
        R8I (0x8231),
        R8UI (0x8232),
        R16I (0x8233),
        R16UI (0x8234),
        R32I (0x8235),
        R32UI (0x8236),

        //RED GREEN
        RG (0x8227),
        RG8 (0x822B),
        RG8_SNROM (0x8F95),
        RG16 (0x822C),
        RG16_SNROM (0x8F99),
        RG16F (0x822F),
        RG32F (0x8230),
        RG8I (0x8237),
        RG8UI (0x8238),
        RG16I (0x8239),
        RG16UI (0x823A),
        RG32I (0x823B),
        RG32UI (0x823C),

        //RED GREEN BLUE
        RGB (0x1907),
        R3_G3_B2 (0x2A10),
        RGB4 (0x804F),
        RGB5 (0x8050),
        RGB8 (0x8051),
        RGB8_SNORM (0x8F96),
        RGB10 (0x8052),
        RGB12 (0x8053),
        RGB16 (0x8054),
        RGB16_SNROM (0x8F9A),
        SRGB (0x8C40),
        SRGB8 (0x8C41),
        RGB16F (0x881B),
        RGB32F (0x8815),
        R11F_G11F_B10F (0x8C3A),
        RGB9_E5 (0x8C3D),
        RGB8I (0x8D8F),
        RGB8UI (0x8D7D),
        RGB16I (0x8D89),
        RGB16UI (0x8D77),
        RGB32I (0x8D83),
        RGB32UI (0x8D71),

        //RED GREEN BLUE ALPHA
        RGBA (0x1908),
        RGBA2 (0x8055),
        RGBA4 (0x8056),
        RGB5_A1 (0x8057),
        RGBA8 (0x8058),
        RGBA8_SNORM (0x8F97),
        RGB10_A2 (0x8059),
        RGB10_A2UI (0x906F),
        RGBA12 (0x805A),
        RGBA16 (0x805B),
        RGBA16_SNROM (0x8F9B),
        SRGBA (0x8C42),
        SRGBA8 (0x8C43),
        RGBA16F (0x881A),
        RGBA32F (0x8815),
        RGBA8I (0x8D8E),
        RGBA8UI (0x8D7C),
        RGBA16I (0x8D88),
        RGBA16UI (0x8D76),
        RGBA32I (0x8D82),
        RGBA32UI (0x8D70),

        //Others
        DEPTH_COMPONENT (0x1902),
        DEPTH_COMPONENT16 (33189),
        DEPTH_COMPONENT24 (33190),
        DEPTH_COMPONENT32 (33191),
        DEPTH_COMPONENT32F (0x8CAC),
        DEPTH_STENCIL (0x84F9),
        STENCIL_INDEX (0x1901),
        STENCIL_INDEX1 (0x8D46),
        STENCIL_INDEX4 (0x8D47),
        STENCIL_INDEX8 (0x8D48),
        STENCIL_INDEX16 (0x8D49),
        DEPTH24_STENCIL8 (0x8CAD);

        final int e;
        TextureFormat(int t) { e = t; }
    }

    /**
     * Enum with external formats for Textures
     * @author melchor9000
     **/
    enum TextureExternalFormat {
        RED (0x1903),
        RG (0x8227),
        RGB (0x1907),
        BGR (0x80E0),
        RGBA (0x1908),
        BGRA (0x80E1),
        RED_INTEGER (0x8D94),
        RG_INTEGER (0x8228),
        RGB_INTEGER (0x8D98),
        BGR_INTEGER (0x8D9A),
        RGBA_INTEGER (0x8D99),
        BGRA_INTEGER (0x8D9B),
        DEPTH_COMPONENT (0x1902),
        DEPTH_STENCIL (0x84F9);

        final int e;
        TextureExternalFormat(int t) { e = t; }
    }

    /**
     * Enum for parameters for GL_TEXTURE_WRAP_(S/T)
     * @author melchor9000
     */
    enum TextureWrap {
        CLAMP (0x2900),
        CLAMP_TO_EDGE (0x812F),
        CLAMP_TO_BORDER (0x812D),
        REPEAT (0x2901);

        final int e;
        TextureWrap(int t) { e = t; }
    }

    /**
     * Enum for texture filters
     * @author melchor9000
     */
    enum TextureFilter {
        NEAREST (0x2600),
        LINEAR (0x2601),
        NEAREST_MIPMAP_NEAREST (0x2700),
        LINEAR_MIPMAP_NEAREST (0x2701),
        NEAREST_MIPMAP_LINEAR (0x2702),
        LINEAR_MIPMAP_LINEAR (0x2703);

        final int e;
        TextureFilter(int t) { e = t; }
    }

    /**
     * Enum for texture parameters (glTexParameteri).
     * Deleted prefix <i>GL_TEXTURE</i>
     * @author melchor9000
     */
    enum TextureParameter {
        BASE_LEVEL (0x813C),
        COMPARE_FUNC (0x884D),
        COMPARE_MODE (0x884C),
        LOD_BIAS (0x8501),
        MIN_FILTER (0x2801),
        MAG_FILTER (0x2800),
        MIN_LOD (0x813A),
        MAX_LOD (0x813B),
        MAX_LEVEL (0x813D),
        SWIZZLE_R (0x8E42),
        SWIZZLE_G (0x8E43),
        SWIZZLE_B (0x8E44), 
        SWIZZLE_A (0x8E45),
        WRAP_S (0x2802),
        WRAP_T (0x2803),
        WRAP_R (0x8072);

        final int e;
        TextureParameter(int t) { e = t; }
    }

    /**
     * Enum for draw modes
     * @author melchor9000
     **/
    enum DrawMode {
        POINTS (0x0),
        LINES (0x1),
        LINE_LOOP (0x2),
        LINE_STRIP (0x3),
        TRIANGLES (0x4),
        TRIANGLE_STRIP (0x5),
        TRIANGLE_FAN (0x6),
        LINES_ADJACENCY (0xA),
        LINE_STRIP_ADJACENCY (0xB),
        TRIANGLES_ADJACENCY (0xC),
        TRIANGLE_STRIP_ADJACENCY (0xD);

        final int e;
        DrawMode(int t) { e = t; }
    }

    /**
     * Enum with framebuffers attachments
     * TODO todos los Color_att posibles (al parecer hasta 32)
     * @author melchor9000
     **/
    enum FramebufferAttachment {
        COLOR_ATTACHMENT (0x8CE0),
        DEPTH_ATTACHMENT (0x8D00),
        STENCIL_ATTACHMENT (0x8D20),
        DEPTH_STENCIL_ATTACHMENT (0x821A),


        COLOR_ATTACHMENT1 (0x8CE1),
        COLOR_ATTACHMENT2 (0x8CE2),
        COLOR_ATTACHMENT3 (0x8CE3),
        COLOR_ATTACHMENT4 (0x8CE4),
        COLOR_ATTACHMENT5 (0x8CE5),
        COLOR_ATTACHMENT6 (0x8CE6),
        COLOR_ATTACHMENT7 (0x8CE7),
        COLOR_ATTACHMENT8 (0x8CE8),;

        final int e;
        FramebufferAttachment(int t) { e = t; }
    }

    /**
     * Enum with framebuffer status
     * @author melchor9000
     **/
    enum FramebufferStatus {
        COMPLETE (0x8CD5),
        INCOMPLETE_ATTACHMENT (0x8CD6),
        INCOMPLETE_MISSING_ATTACHMENT (0x8CD7),
        INCOMPLETE_DRAW_BUFFER (0x8CDB),
        INCOMPLETE_READ_BUFFER (0x8CDC),
        UNSUPPORTED (0x8CDD),
        UNDEFINED (0x8219),
        INCOMPLETE_MULTISAMPLE (0x8D56),
        INCOMPLETE_LAYER_TARGETS (0x8DA8);

        final int e;
        FramebufferStatus(int t) { e = t; }
        @Override public String toString() {
            String a = this.name();
            return "GL_FRAMEBUFFER_" + a;
        }
    }

    enum StencilFunc {
        NEVER (0x200),
        LESS (0x201),
        EQUAL (0x202),
        LEQUAL (0x203),
        GREATER (0x204),
        NOTEQUAL (0x205),
        GEQUAL (0x206),
        ALWAYS (0x207);

        final int e;
        StencilFunc(int t) { e = t; }
    }

    enum StencilOp {
        KEEP (0x1e00),
        ZERO (0x0),
        REPLACE (0x1e01),
        INCR (0x1e02),
        INCR_WRAP (0x8507),
        DECR (0x1e03),
        DECR_WRAP (0x8508),
        INVERT (0x150a);

        final int e;
        StencilOp(int t) { e = t; }
    }

    enum CullFaceMode {
        BACK, FRONT, FRONT_AND_BACK
    }

    /**
     * Enum with all possible values for {@link #getBoolean(GLGet)},
     * {@link #getInt(GLGet)}, {@link #getLong(GLGet)}, {@link #getInt64(GLGet)},
     * {@link #getFloat(GLGet)}, {@link #getDouble(GLGet)} and its v variants.
     */
    enum GLGet {
        ACTIVE_TEXTURE (0x84E0),
        ALIASED_LINE_WIDTH_RANGE (0x846E),
        ARRAY_BUFFER_BINDING (0x8894),
        BLEND (0xBE2),
        BLEND_COLOR (0x8005),
        BLEND_DST_ALPHA (0x80CA),
        BLEND_DST_RGB (0x80C8),
        BLEND_EQUATION_RGB (0x8009),
        BLEND_EQUATION_ALPHA (0x883D),
        BLEND_SRC_ALPHA (0x80CB),
        BLEND_SRC_RGB (0x80C9),
        COLOR_CLEAR_VALUE (0xC22),
        COLOR_LOGIC_OP (0xBF2),
        COLOR_WRITEMASK (0xC23),
        COMPRESSED_TEXTURE_FORMATS (0x86A3),
        MAX_COMPUTE_SHADER_STORAGE_BLOCKS (0x90DB),
        MAX_COMBINED_SHADER_STORAGE_BLOCKS (0x90DC),
        MAX_COMPUTE_UNIFORM_BLOCKS (0x91BB),
        MAX_COMPUTE_TEXTURE_IMAGE_UNITS (0x91BC),
        MAX_COMPUTE_UNIFORM_COMPONENTS (0x8263),
        MAX_COMPUTE_ATOMIC_COUNTERS (0x8265),
        MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS (0x8264),
        MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS (0x8266),
        MAX_COMPUTE_WORK_GROUP_INVOCATIONS (0x90EB),
        MAX_COMPUTE_WORK_GROUP_COUNT (0x91BE),
        MAX_COMPUTE_WORK_GROUP_SIZE (0x91BF),
        DISPATCH_INDIRECT_BUFFER_BINDING (0x90EF),
        MAX_DEBUG_GROUP_STACK_DEPTH (0x826C),
        DEBUG_GROUP_STACK_DEPTH (0x826D),
        CONTEXT_FLAGS (0x821E),
        CULL_FACE (0xB44),
        CURRENT_PROGRAM (0x8B8D),
        DEPTH_CLEAR_VALUE (0xB73),
        DEPTH_FUNC (0xB74),
        DEPTH_RANGE (0xB70),
        DEPTH_TEST (0xB71),
        DEPTH_WRITEMASK (0xB72),
        DITHER (0xBD0),
        DOUBLEBUFFER (0xC32),
        DRAW_BUFFER (0xC01),
        DRAW_FRAMEBUFFER_BINDING (0x8CA6),
        READ_FRAMEBUFFER_BINDING (0x8CAA),
        ELEMENT_ARRAY_BUFFER_BINDING (0x8895),
        FRAGMENT_SHADER_DERIVATIVE_HINT (0x8B8B),
        IMPLEMENTATION_COLOR_READ_FORMAT (0x8B9B),
        IMPLEMENTATION_COLOR_READ_TYPE (0x8B9A),
        LINE_SMOOTH (0xB20),
        LINE_SMOOTH_HINT (0xC52),
        LINE_WIDTH (0xB21),
        LAYER_PROVOKING_VERTEX (0x825E),
        LOGIC_OP_MODE (0xBF0),
        MAJOR_VERSION (0x821B),
        MAX_3D_TEXTURE_SIZE (0x8073),
        MAX_ARRAY_TEXTURE_LAYERS (0x88FF),
        MAX_CLIP_DISTANCES (0xD32),
        MAX_COLOR_TEXTURE_SAMPLES (0x910E),
        MAX_COMBINED_ATOMIC_COUNTERS (0x92D7),
        MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS (0x8A33),
        MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS (0x8A32),
        MAX_COMBINED_TEXTURE_IMAGE_UNITS (0x8B4D),
        MAX_COMBINED_UNIFORM_BLOCKS (0x8A2E),
        MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS (0x8A31),
        MAX_CUBE_MAP_TEXTURE_SIZE (0x851C),
        MAX_DEPTH_TEXTURE_SAMPLES (0x910F),
        MAX_DRAW_BUFFERS (0x8824),
        MAX_DUAL_SOURCE_DRAW_BUFFERS (0x88FC),
        MAX_ELEMENTS_INDICES (0x80E9),
        MAX_ELEMENTS_VERTICES (0x80E8),
        MAX_FRAGMENT_ATOMIC_COUNTERS (0x92D6),
        MAX_FRAGMENT_SHADER_STORAGE_BLOCKS (0x90DA),
        MAX_FRAGMENT_INPUT_COMPONENTS (0x9125),
        MAX_FRAGMENT_UNIFORM_COMPONENTS (0x8B49),
        MAX_FRAGMENT_UNIFORM_VECTORS (0x8DFD),
        MAX_FRAGMENT_UNIFORM_BLOCKS (0x8A2D),
        MAX_FRAMEBUFFER_WIDTH (0x9315),
        MAX_FRAMEBUFFER_HEIGHT (0x9316),
        MAX_FRAMEBUFFER_LAYERS (0x9317),
        MAX_FRAMEBUFFER_SAMPLES (0x9318),
        MAX_GEOMETRY_ATOMIC_COUNTERS (0x92D5),
        MAX_GEOMETRY_SHADER_STORAGE_BLOCKS (0x90D7),
        MAX_GEOMETRY_INPUT_COMPONENTS (0x9123),
        MAX_GEOMETRY_OUTPUT_COMPONENTS (0x9124),
        MAX_GEOMETRY_TEXTURE_IMAGE_UNITS (0x8C29),
        MAX_GEOMETRY_UNIFORM_BLOCKS (0x8A2C),
        MAX_GEOMETRY_UNIFORM_COMPONENTS (0x8DDF),
        MAX_INTEGER_SAMPLES (0x9110),
        MIN_MAP_BUFFER_ALIGNMENT (0x90BC),
        MAX_LABEL_LENGTH (0x82E8),
        MAX_PROGRAM_TEXEL_OFFSET (0x8905),
        MIN_PROGRAM_TEXEL_OFFSET (0x8904),
        MAX_RECTANGLE_TEXTURE_SIZE (0x84F8),
        MAX_RENDERBUFFER_SIZE (0x84E8),
        MAX_SAMPLE_MASK_WORDS (0x8E59),
        MAX_SERVER_WAIT_TIMEOUT (0x9111),
        MAX_SHADER_STORAGE_BUFFER_BINDINGS (0x90DD),
        MAX_TESS_CONTROL_ATOMIC_COUNTERS (0x92D3),
        MAX_TESS_EVALUATION_ATOMIC_COUNTERS (0x92D4),
        MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS (0x90D8),
        MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS (0x90D9),
        MAX_TEXTURE_BUFFER_SIZE (0x8C2B),
        MAX_TEXTURE_IMAGE_UNITS (0x8872),
        MAX_TEXTURE_LOD_BIAS (0x84FD),
        MAX_TEXTURE_SIZE (0xD33),
        MAX_UNIFORM_BUFFER_BINDINGS (0x8A2F),
        MAX_UNIFORM_BLOCK_SIZE (0x8A30),
        MAX_UNIFORM_LOCATIONS (0x826E),
        MAX_VARYING_COMPONENTS (0x8B4B),
        MAX_VARYING_VECTORS (0x8DFC),
        MAX_VARYING_FLOATS (0x8B4B),
        MAX_VERTEX_ATOMIC_COUNTERS (0x92D2),
        MAX_VERTEX_ATTRIBS (0x8869),
        MAX_VERTEX_SHADER_STORAGE_BLOCKS (0x90D6),
        MAX_VERTEX_TEXTURE_IMAGE_UNITS (0x8B4C),
        MAX_VERTEX_UNIFORM_COMPONENTS (0x8B4A),
        MAX_VERTEX_UNIFORM_VECTORS (0x8DFB),
        MAX_VERTEX_OUTPUT_COMPONENTS (0x9122),
        MAX_VERTEX_UNIFORM_BLOCKS (0x8A2B),
        MAX_VIEWPORT_DIMS (0xD3A),
        MAX_VIEWPORTS (0x825B),
        MINOR_VERSION (0x821C),
        NUM_COMPRESSED_TEXTURE_FORMATS (0x86A2),
        NUM_EXTENSIONS (0x821D),
        NUM_PROGRAM_BINARY_FORMATS (0x87FE),
        NUM_SHADER_BINARY_FORMATS (0x8DF9),
        PACK_ALIGNMENT (0xD05),
        PACK_IMAGE_HEIGHT (0x806C),
        PACK_LSB_FIRST (0xD01),
        PACK_ROW_LENGTH (0xD02),
        PACK_SKIP_IMAGES (0x806B),
        PACK_SKIP_PIXELS (0xD04),
        PACK_SKIP_ROWS (0xD03),
        PACK_SWAP_BYTES (0xD00),
        PIXEL_PACK_BUFFER_BINDING (0x88ED),
        PIXEL_UNPACK_BUFFER_BINDING (0x88EF),
        POINT_FADE_THRESHOLD_SIZE (0x8128),
        PRIMITIVE_RESTART_INDEX (0x8F9E),
        PROGRAM_BINARY_FORMATS (0x87FF),
        PROGRAM_PIPELINE_BINDING (0x825A),
        PROGRAM_POINT_SIZE (0x8642),
        PROVOKING_VERTEX (0x8E4F),
        POINT_SIZE (0xB11),
        POINT_SIZE_GRANULARITY (0xB13),
        POINT_SIZE_RANGE (0xB12),
        POLYGON_OFFSET_FACTOR (0x8038),
        POLYGON_OFFSET_UNITS (0x2A00),
        POLYGON_OFFSET_FILL (0x8037),
        POLYGON_OFFSET_LINE (0x2A02),
        POLYGON_OFFSET_POINT (0x2A01),
        POLYGON_SMOOTH (0xB41),
        POLYGON_SMOOTH_HINT (0xC53),
        READ_BUFFER (0xC02),
        RENDERBUFFER_BINDING (0x8CA7),
        SAMPLE_BUFFERS (0x80A8),
        SAMPLE_COVERAGE_VALUE (0x80AA),
        SAMPLE_COVERAGE_INVERT (0x80AB),
        SAMPLER_BINDING (0x8919),
        SAMPLES (0x80A9),
        SCISSOR_BOX (0xC10),
        SCISSOR_TEST (0xC11),
        SHADER_COMPILER (0x8DFA),
        SHADER_STORAGE_BUFFER_BINDING (0x90D3),
        SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT (0x90DF),
        SHADER_STORAGE_BUFFER_START (0x90D4),
        SHADER_STORAGE_BUFFER_SIZE (0x90D5),
        SMOOTH_LINE_WIDTH_RANGE (0xB22),
        SMOOTH_LINE_WIDTH_GRANULARITY (0xB23),
        //STENCIL_BACK_FAIL (GL_STENCIL_BACK_FAIL),
        //STENCIL_BACK_FUNC (GL_STENCIL_BACK_FUNC),
        //STENCIL_BACK_PASS_DEPTH_FAIL (GL_STENCIL_BACK_PASS_DEPTH_FAIL),
        //STENCIL_BACK_PASS_DEPTH_PASS (GL_STENCIL_BACK_PASS_DEPTH_PASS),
        STENCIL_BACK_REF (0x8CA3),
        STENCIL_BACK_VALUE_MASK (0x8CA4),
        STENCIL_BACK_WRITEMASK (0x8CA5),
        STENCIL_CLEAR_VALUE (0xB91),
        STENCIL_FAIL (0xB94),
        STENCIL_FUNC (0xB92),
        STENCIL_PASS_DEPTH_FAIL (0xB95),
        STENCIL_PASS_DEPTH_PASS (0xB96),
        STENCIL_REF (0xB97),
        STENCIL_TEST (0xB90),
        STENCIL_VALUE_MASK (0xB93),
        STENCIL_WRITEMASK (0xB98),
        STEREO (0xC33),
        SUBPIXEL_BITS (0xD50),
        TEXTURE_BINDING_1D (0x8068),
        TEXTURE_BINDING_1D_ARRAY (0x8C1C),
        TEXTURE_BINDING_2D (0x8069),
        TEXTURE_BINDING_2D_ARRAY (0x8C1D),
        TEXTURE_BINDING_2D_MULTISAMPLE (0x9104),
        TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY (0x9105),
        TEXTURE_BINDING_3D (0x806A),
        TEXTURE_BINDING_CUBE_MAP (0x8514),
        TEXTURE_BINDING_RECTANGLE (0x84F6),
        TEXTURE_COMPRESSION_HINT (0x84EF),
        TEXTURE_BINDING_BUFFER (0x8C2C),
        TEXTURE_BUFFER_OFFSET_ALIGNMENT (0x919F),
        TIMESTAMP (0x8E28),
        TRANSFORM_FEEDBACK_BUFFER_BINDING (0x8C8F),
        TRANSFORM_FEEDBACK_BUFFER_START (0x8C84),
        TRANSFORM_FEEDBACK_BUFFER_SIZE (0x8C85),
        UNIFORM_BUFFER_BINDING (0x8A28),
        UNIFORM_BUFFER_OFFSET_ALIGNMENT (0x8A34),
        UNIFORM_BUFFER_SIZE (0x8A2A),
        UNIFORM_BUFFER_START (0x8A29),
        UNPACK_ALIGNMENT (0xCF5),
        UNPACK_IMAGE_HEIGHT (0x806E),
        UNPACK_LSB_FIRST (0xCF1),
        UNPACK_ROW_LENGTH (0xCF2),
        UNPACK_SKIP_IMAGES (0x806D),
        UNPACK_SKIP_PIXELS (0xCF4),
        UNPACK_SKIP_ROWS (0xCF3),
        UNPACK_SWAP_BYTES (0xCF0),
        VERTEX_ARRAY_BINDING (0x85B5),
        VERTEX_BINDING_DIVISOR (0x82D6),
        VERTEX_BINDING_OFFSET (0x82D7),
        VERTEX_BINDING_STRIDE (0x82D8),
        MAX_VERTEX_ATTRIB_RELATIVE_OFFSET (0x82D9),
        MAX_VERTEX_ATTRIB_BINDINGS (0x82DA),
        VIEWPORT (0xBA2),
        VIEWPORT_BOUNDS_RANGE (0x825D),
        VIEWPORT_INDEX_PROVOKING_VERTEX (0x825F),
        VIEWPORT_SUBPIXEL_BITS (0x825C),
        MAX_ELEMENT_INDEX (0x8D6B),
        MAX_COLOR_ATTACHMENTS (36063); //TODO todo

        final int e;
        GLGet(int t) { e = t; }
    }
    
    /**
     * Values returned by calling {@link #getError()}
     * @author melchor9000
     */
    enum Error {
        /**
         * No error has been recorded
         */
        NO_ERROR(0),
        
        /**
         * An unacceptable value is specified for an enumerated argument.
         * The offending command is ignored and has no other side effect
         * than to set the error flag.
         */
        INVALID_ENUM(1280),

        /**
         * A numeric argument is out of range. The offending command is
         * ignored and has no other side effect than to set the error flag.
         */
        INVALID_VALUE(1281),
        
        /**
         * The specified operation is not allowed in the current state.
         * The offending command is ignored and has no other side effect
         * than to set the error flag.
         */
        INVALID_OPERATION(1282),
        
        /**
         * The framebuffer object is not complete. The offending command
         * is ignored and has no other side effect than to set the error flag.
         */
        INVALID_FRAMEBUFFER_OPERATION(1286),
        
        /**
         * There is not enough memory left to execute the command. The
         * state of the GL is undefined, except for the state of the
         * error flags, after this error is recorded.
         */
        OUT_OF_MEMORY(1285),
        
        /**
         * An attempt has been made to perform an operation that would
         * cause an internal stack to overflow.
         */
        STACK_OVERFLOW(1283),
        
        /**
         * An attempt has been made to perform an operation that would
         * cause an internal stack to underflow.
         */
        STACK_UNDERFLOW(1284);
        
        public final int errno;
        Error(int t) { errno = t; }
    }

    /**
     * Enum for glGetBufferParameteriv/glGetBufferParameteri64v
     * @author melchor9000
     **/
    enum GLGetBuffer {
        BUFFER_ACCESS (0x88BB),
        BUFFER_MAPPED (0x88BC),
        BUFFER_SIZE (0x8764),
        BUFFER_USAGE (0x8765);

        final int e;
        GLGetBuffer(int t) { e = t; }
    }

    /**
     * Enum for glGetShaderiv
     * @author melchor9000
     **/
    enum GLGetShader {
        SHADER_TYPE (0x8B4F),
        DELETE_STATUS (0x8B80),
        COMPILE_STATUS (0x8B81),
        INFO_LOG_LENGTH (0x8B84),
        SHADER_SOURCE_LENGTH (0x8B88);

        final int e;
        GLGetShader(int t) { e = t; }
    }

    /**
     * Enum for glGetProgramiv
     * @author melchor9000
     **/
    enum GLGetProgram {
        DELETE_STATUS (0x8B80),
        LINK_STATUS (0x8B82),
        VALIDATE_STATUS (0x8B83),
        INFO_LOG_LENGTH (0x8B84),
        ATTACHED_SHADERS (0x8B85),
        ACTIVE_ATTRIBUTES (0x8B89),
        ACTIVE_ATTRIBUTE_MAX_LENGTH (0x8B8A),
        ACTIVE_UNIFORMS (0x8B86),
        ACTIVE_UNIFORM_BLOCKS (0x8A36),
        ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH (0x8A35),
        ACTIVE_UNIFORM_MAX_LENGTH (0x8B87),
        TRANSFORM_FEEDBACK_BUFFER_MODE (0x8C7F),
        TRANSFORM_FEEDBACK_VARYINGS (0x8C83),
        TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH (0x8C76),
        GEOMETRY_VERTICES_OUT (0x8DDA),
        GEOMETRY_INPUT_TYPE (0x8DDB),
        GEOMETRY_OUTPUT_TYPE (0x8DDC);

        final int e;
        GLGetProgram(int t) { e = t; }
    }

    /**
     * Enum for glBlendFunc and glBlendFuncSeparate
     * @author melchor9000
     */
    enum BlendOption {
        ZERO (0),
        ONE (1),
        SRC_COLOR (0x300),
        ONE_MINUS_SRC_COLOR (0x301),
        DST_COLOR (0x306),
        ONE_MINUS_DST_COLOR (0x307),
        SRC_ALPHA (0x302),
        ONE_MINUS_SRC_ALPHA (0x303),
        DST_ALPHA (0x304),
        ONE_MINUS_DST_ALPHA (0x305),
        CONSTANT_COLOR (0x8001),
        ONE_MINUS_CONSTANT_COLOR (0x8002),
        CONSTANT_ALPHA (0x8003),
        ONE_MINUS_CONSTANT_ALPHA (0x8004);

        final int e;
        BlendOption(int t) { e = t; }
    }

    enum BlendEquation {
        ADD (0x8006),
        SUSTRACT (0x800A),
        REVERSE_SUBSTRACT (0x800B);

        final int e;
        BlendEquation(int t) { e = t; }
    }

    int COLOR_CLEAR_BIT = 0x4000;
    int DEPTH_BUFFER_BIT = 0x100;
    int STENCIL_BUFFER_BIT = 0x400;

    /**
     * Destroys the current context. Any OpenGL api won't
     * work after that.
     */
    void destroyContext();

    /**
     * Checks if the current context has an extension capability.
     * If the extension name is invalid, the method will return
     * false.
     * @param name name of the extension
     * @return true if has the extension
     */
    boolean hasCapability(String name);

    /**
     * Enable a capability
     * @param enable Capability to enable
     * @see GLEnable
     */
    void enable(GLEnable enable);

    /**
     * Disable a capability
     * @param disable Capability to disable
     * @see GLEnable
     */
    void disable(GLEnable disable);

    /**
     * Determine if a capability is enabled or not
     * @param enabled Capability to test
     * @return True if is activated, false otherwise
     */
    boolean isEnabled(GLEnable enabled);
    
    /**
     * Each detectable error is assigned a numeric
     * code and symbolic name. When an error occurs,
     * the error flag is set to the appropriate error
     * code value. All values returner by this method
     * are in {@link GLContext.Error} enum.
     * @return the value of the error flag
     */
    Error getError();

    //Vertex Arrays
    int genVertexArray();
    void genVertexArrays(int[] buff);
    void deleteVertexArray(int vao);
    void deleteVertexArrays(int[] buff);
    void bindVertexArray(int vao);

    //Buffer Objects
    int genBuffer();
    void genBuffers(int[] buff);
    void deleteBuffer(int vbo);
    void deleteBuffers(int[] ebo);
    void bindBuffer(BufferTarget target, int bo);
    void bufferData(BufferTarget target, int count, BufferUsage usage);
    void bufferData(BufferTarget target, byte[] buff, BufferUsage usage);
    void bufferData(BufferTarget target, short[] buff, BufferUsage usage);
    void bufferData(BufferTarget target, int[] buff, BufferUsage usage);
    void bufferData(BufferTarget target, float[] buff, BufferUsage usage);
    void bufferData(BufferTarget target, double[] buff, BufferUsage usage);
    void bufferData(BufferTarget target, ByteBuffer buff, BufferUsage usage);
    void bufferData(BufferTarget target, ShortBuffer buff, BufferUsage usage);
    void bufferData(BufferTarget target, IntBuffer buff, BufferUsage usage);
    void bufferData(BufferTarget target, FloatBuffer buff, BufferUsage usage);
    void bufferData(BufferTarget target, DoubleBuffer buff, BufferUsage usage);
    void bufferSubData(BufferTarget target, long offset, byte[] buff);
    void bufferSubData(BufferTarget target, long offset, short[] buff);
    void bufferSubData(BufferTarget target, long offset, int[] buff);
    void bufferSubData(BufferTarget target, long offset, float[] buff);
    void bufferSubData(BufferTarget target, long offset, double[] buff);
    void bufferSubData(BufferTarget target, long offset, ByteBuffer buff);
    void bufferSubData(BufferTarget target, long offset, ShortBuffer buff);
    void bufferSubData(BufferTarget target, long offset, IntBuffer buff);
    void bufferSubData(BufferTarget target, long offset, FloatBuffer buff);
    void bufferSubData(BufferTarget target, long offset, DoubleBuffer buff);
    void drawArrays(DrawMode mode, int first, int count);
    void drawArraysInstanced(DrawMode mode, int first, int count, int times);
    void drawElements(DrawMode mode, int length, type type, long offset);
    void drawElementsInstanced(DrawMode mode, int length, type type, long offset, int times);
    int getBufferParameteri(BufferTarget target, GLGetBuffer param);
    long getBufferParameteri64(BufferTarget target, GLGetBuffer param);

    //Shaders
    int createShader(ShaderType type);
    int createProgram();
    void deleteShader(int shader);
    void deleteProgram(int program);

    void shaderSource(int shader, String src);
    void compileShader(int shader);
    void attachShader(int program, int shader);
    void linkProgram(int program);
    void useProgram(int program);

    int getShader(int shader, GLGetShader pName);
    String getShaderInfoLog(int shader);
    int getProgram(int program, GLGetProgram pName);
    String getProgramInfoLog(int program);

    int getAttribLocation(int program, String name);
    void enableVertexAttribArray(int loc);
    void vertexAttribPointer(int loc, int size, type type, boolean norm, int stride, long off);
    void bindFragDataLocation(int program, int colorNumber, String name);
    String getActiveAttrib(int program, int pos, int strlen);
    int getActiveAttribSize(int program, int pos);
    int getActiveAttribType(int program, int pos);
    void vertexAttribDivisor(int loc, int divisor);

    int getUniformLocation(int program, String name);
    String getActiveUniform(int program, int pos, int strlen);
    void uniform1f(int loc, float value);
    void uniform1i(int loc, int value);
    void uniform2f(int loc, float v1, float v2);
    void uniform2i(int loc, int v1, int v2);
    void uniform3f(int loc, float v1, float v2, float v3);
    void uniform3i(int loc, int v1, int v2, int v3);
    void uniform4f(int loc, float v1, float v2, float v3, float v4);
    void uniform4i(int loc, int v1, int v2, int v3, int v4);
    void uniformMatrix2(int loc, boolean trans, float[] matrix) throws BufferUnderflowException;
    //void uniformMatrix2(int loc, mat2 matrix); TODO
    void uniformMatrix3(int loc, boolean trans, float[] matrix) throws BufferUnderflowException;
    //void uniformMatrix3(int loc, mat3 matrix); TODO
    void uniformMatrix4(int loc, boolean trans, float[] matrix) throws BufferUnderflowException;
    //void uniformMatrix4(int loc, mat4 matrix); TODO

    //Texturas
    void setActiveTexture(int i);
    short getMaxTextureUnits();
    int genTexture();
    void genTextures(int[] texs);
    void deleteTexture(int tex);
    void deleteTextures(int[] texs);
    void generateMipmap(TextureTarget t);
    void bindTexture(TextureTarget target, int tex);
    void texParameteri(TextureTarget target, TextureParameter pName, int param);
    void texParameteri(TextureTarget target, TextureParameter pName, TextureWrap p);
    void texParameteri(TextureTarget target, TextureParameter pName, TextureFilter p);
    void copyTexImage1D(TextureTarget t, int level, TextureFormat ifmt, int x, int y, int width);
    void copyTexImage2D(TextureTarget t, int level, TextureFormat ifmt, int x, int y, int width, int height);

    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, byte[] b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, short[] b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, int[] b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, float[] b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, double[] b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, ByteBuffer b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, ShortBuffer b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, IntBuffer b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, FloatBuffer b);
    void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, DoubleBuffer b);

    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, byte[] b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, short[] b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, int[] b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, float[] b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, double[] b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, ByteBuffer b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, ShortBuffer b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, IntBuffer b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, FloatBuffer b);
    void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, DoubleBuffer b);

    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, byte[] b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, short[] b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, int[] b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, float[] b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, double[] b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, ByteBuffer b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, ShortBuffer b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, IntBuffer b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, FloatBuffer b);
    void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, DoubleBuffer b);

    //Framebuffer & Renderbuffer
    int genFramebuffer();
    void genFramebuffers(int[] fbs);
    void deleteFramebuffer(int fb);
    void deleteFramebuffers(int[] fbs);
    int genRenderbuffer();
    void genRenderbuffers(int[] fbs);
    void deleteRenderbuffer(int rb);
    void deleteRendebuffers(int[] rbs);
    void bindFramebuffer(int framebuffer);
    void bindRenderbuffer(int rbo);
    FramebufferStatus checkFramebufferStatus();

    void framebufferTexture1D(FramebufferAttachment at, TextureTarget tg, int tex, int level);
    void framebufferTexture2D(FramebufferAttachment at, TextureTarget tg, int tex, int level);
    void framebufferTexture3D(FramebufferAttachment at, TextureTarget tg, int tex, int level, int z);
    void framebufferRenderbuffer(FramebufferAttachment at, int rbo);

    void renderbufferStorage(TextureFormat fmt, int width, int height);
    void renderbufferStorageMultisample(TextureFormat fmt, int samples, int w, int h);

    //Stencil utils
    void stencilFunc(StencilFunc func, int ref, int mask);
    void stencilOp(StencilOp sfail, StencilOp dfail, StencilOp pass);
    void stencilMask(int mask);

    //General utils and stuff
    void depthMask(boolean a);
    void clear(int mask);
    void clearColor(float r, float g, float b, float a);
    void viewport(int x, int y, int width, int height);
    void colorMask(boolean r, boolean g, boolean b, boolean a);
    void cullFace(CullFaceMode mode);
    void blendFunc(BlendOption sourceFactor, BlendOption destinationFactor);
    void blendFuncSeparate(BlendOption rgbSrcFactor, BlendOption rgbDestFactor, BlendOption aSrcFactor, BlendOption aDestFactor);
    void blendEquation(BlendEquation eq);
    void blendEquationSeparate(BlendEquation colorEq, BlendEquation alphaEq);
    boolean getBoolean(GLGet get);
    int getInt(GLGet get);
    long getLong(GLGet get);
    float getFloat(GLGet get);
    double getDouble(GLGet get);
    default long getInt64(GLGet get) { return getLong(get); }
    void getBoolean(GLGet get, boolean[] v);
    void getInt(GLGet get, int[] v);
    void getLong(GLGet get, long[] v);
    void getFloat(GLGet get, float[] v);
    void getDouble(GLGet get, double[] v);
    default long getInt64(GLGet get, long[] v) { getLong(get, v); }
    void readBuffer(CullFaceMode mode);
    void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, ByteBuffer data);
    void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, ShortBuffer data);
    void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, IntBuffer data);
    void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, FloatBuffer data);

    /**
     * Function called at the end of the game loop
     * @param fps Frame per second of the game (maybe is util)
     */
    void _game_loop_sync(int fps);
}
